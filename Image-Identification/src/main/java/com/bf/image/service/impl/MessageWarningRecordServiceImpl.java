package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.pojo.*;
import com.bf.image.service.*;
import com.bf.image.mapper.MessageWarningRecordMapper;
import com.bf.image.utils.DingUtils;
import com.bf.image.vo.MessageWarningRecordVo;
import com.bf.image.vo.TevVo;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bf.image.utils.JsonParser.jsonParser;

/**
* @author Gplus-033
* @description 针对表【message_warning_record】的数据库操作Service实现
* @createDate 2024-05-06 12:28:27
*/
@Service
public class MessageWarningRecordServiceImpl extends ServiceImpl<MessageWarningRecordMapper, MessageWarningRecord>
    implements MessageWarningRecordService{

    @Autowired
    private MessageWarningService messageWarningService;

    @Autowired
    private DetailInformationService detailService;

    @Autowired
    private TevInformationService tevService;

    @Autowired
    private ImageInformationService imageService;

    @Autowired
    private UserInformationService userService;

    @Autowired
    private DeviceInformationService deviceService;

    @Autowired
    private WorkOrderService workOrderService;


    @Override
    public Page<MessageWarningRecordVo> pageVo(MessageWarningRecordVo messageWarningRecordVo) {

        Integer pageSize = messageWarningRecordVo.getPageSize();
        Integer offset = (messageWarningRecordVo.getCurrent() - 1) * pageSize;
        Page<MessageWarningRecord> newPage = new Page<>();
        newPage.setCurrent(offset);
        newPage.setSize(pageSize);

        Date startTime = null;
        Date endTime = null;

        if (messageWarningRecordVo.getDateRange() != null && org.apache.commons.collections.CollectionUtils.isNotEmpty(messageWarningRecordVo.getDateRange())) {
            startTime = messageWarningRecordVo.getDateRange().get(0);
            endTime = messageWarningRecordVo.getDateRange().get(1);
        }

        String messageName = messageWarningRecordVo.getMessageName();
        String memo = messageWarningRecordVo.getMessageMemo();
        List<MessageWarning> messageWarningList = messageWarningService.list(
                Wrappers.lambdaQuery(MessageWarning.class)
                        .like(StringUtils.isNotBlank(messageName), MessageWarning::getMessageName, messageName)
                        .like(StringUtils.isNotBlank(memo), MessageWarning::getMessageMemo, memo)
        );
        if (CollectionUtils.isEmpty(messageWarningList)) {
            return new Page<>();
        }

        Map<Long, MessageWarning> messageWarningMap = messageWarningList.stream().collect(Collectors.toMap(messageWarning -> messageWarning.getMessageWarningId(), Function.identity()));
        List<Long> messageWarningIdList = messageWarningList.stream().map(messageWarning -> messageWarning.getMessageWarningId()).collect(Collectors.toList());

        Page<MessageWarningRecord> page = this.page(newPage,
                Wrappers.lambdaQuery(MessageWarningRecord.class)
                        .in(MessageWarningRecord::getMessageWarningId, messageWarningIdList)
                        .eq(Objects.nonNull(messageWarningRecordVo.getMessageType()), MessageWarningRecord::getMessageType, messageWarningRecordVo.getMessageType())
                        .eq(Objects.nonNull(messageWarningRecordVo.getMessageBizId()), MessageWarningRecord::getMessageBizId, messageWarningRecordVo.getMessageBizId())
                        .between(Objects.nonNull(startTime), MessageWarningRecord::getCreateTime, startTime, endTime)
                        .orderByDesc(MessageWarningRecord::getCreateTime)
        );


        CollectionType listType = jsonParser.getTypeFactory().constructCollectionType(ArrayList.class, MessageWarningRecordVo.class);
        List<MessageWarningRecordVo> records = jsonParser.convertValue(page.getRecords(), listType);

        if (CollectionUtils.isEmpty(records)) {
            return new Page<>();
        }

        Page<MessageWarningRecordVo> objectPage = new Page<MessageWarningRecordVo>(page.getCurrent(), page.getSize(), page.getTotal());

        records.forEach(record -> {
            Long messageWarningId = record.getMessageWarningId();
            MessageWarning messageWarning = messageWarningMap.get(messageWarningId);
            String messageUrl = messageWarning.getMessageUrl();
            String warningMessageName = messageWarning.getMessageName();
            String messageMemo = messageWarning.getMessageMemo();

            record.setMessageMemo(messageMemo);
            record.setMessageUrl(messageUrl);
            record.setMessageName(warningMessageName);
        });

        objectPage.setRecords(records);

        return objectPage;
    }

    @Transactional
    @Override
    public void sendMsg(MessageWarningRecordVo messageWarningRecordVo) {
        Long messageWarningId = messageWarningRecordVo.getMessageWarningId();
        Long messageBizId = messageWarningRecordVo.getMessageBizId();
        Integer messageType = messageWarningRecordVo.getMessageType();

        MessageWarning messageWarning = messageWarningService.getById(messageWarningId);
        String messageUrl = messageWarning.getMessageUrl();
        WorkOrder workOrder = workOrderService.getOne(Wrappers.lambdaQuery(WorkOrder.class).eq(WorkOrder::getDetailId, messageBizId));

        String msgContent = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (messageType == 1) {
            StringBuilder sb = new StringBuilder();
            DetailInformation detailInformation = detailService.getDetailById(messageBizId);
            Long deviceId = detailInformation.getDevice().getDeviceId();
            DeviceInformation device = deviceService.getById(deviceId);
            Integer deviceType = device.getDeviceType();
            Long imageId = detailInformation.getImage().getImageId();
            String imageName = imageService.getById(imageId).getImageName();

            sb.append("【消息内容】\n");
            sb.append("业务类型：红外图像\n");
            sb.append("用户名称：" + detailInformation.getUsername() + "\n");
            sb.append("领导名称：" + detailInformation.getWorkLeaderName() + "\n");
            sb.append("设备名称：" + device.getDeviceName() + "\n");
            String deviceTypeName = deviceType == 0 ? "开关柜" : deviceType == 1 ? "变压器" : deviceType == 2 ? "电缆" : deviceType == 3 ? "其他" : "-";
            sb.append("设备类型：" + deviceTypeName + "\n");
            sb.append("上传时图像名称：" + imageName + "\n");
            sb.append("供电局：" + detailInformation.getPowerSupplyStation() + "\n");
            sb.append("变电站：" + detailInformation.getSubstationName() + "\n");
            sb.append("配电房：" + detailInformation.getDistributionRoomName() + "\n");
            sb.append("巡检班组：" + detailInformation.getInspectionTeam() + "\n");
            sb.append("环境温度：" + detailInformation.getAmbientTemp() + "°C\n");
            sb.append("相对湿度：" + detailInformation.getRelativeHumidity() + "%\n");
            sb.append("中心湿度：" + detailInformation.getCentralHumidity() + "%\n");
            sb.append("最大温度：" + detailInformation.getMaxTemp() + "°C\n");
            sb.append("最小温度：" + detailInformation.getMinTemp() + "°C\n");
            sb.append("平均温度：" + detailInformation.getAvgTemp() + "°C\n");
            sb.append("反射温度：" + detailInformation.getReflectedTemp() + "°C\n");
            sb.append("馈线：" + detailInformation.getFeeder() + "号线\n");
            sb.append("工单审核状态：失败\n");
            sb.append("工单审核结束时间：" + sdf.format(workOrder.getFailTime()) + "\n");
            sb.append("数据创建时间：" + sdf.format(detailInformation.getCreateTime()) + "\n");
            sb.append("该数据审核未通过，请关注相关数据");
            msgContent = sb.toString();
        } else if (messageType == 2) {
            StringBuilder sb = new StringBuilder();
            TevInformation tevInformation = tevService.getById(messageBizId);
            Long userId = tevInformation.getUserId();
            Long deviceId = tevInformation.getDeviceId();
            Long leaderId = tevInformation.getLeaderId();
            Long imageId = tevInformation.getImageId();

            UserInformation user = userService.getById(userId);
            UserInformation leader = userService.getById(leaderId);
            ImageInformation image = imageService.getById(imageId);
            DeviceInformation device = deviceService.getById(deviceId);

            Integer deviceType = device.getDeviceType();
            String deviceTypeName = deviceType == 0 ? "开关柜" : deviceType == 1 ? "变压器" : deviceType == 2 ? "电缆" : deviceType == 3 ? "其他" : "-";
            String username = user.getUsername();
            String leaderName = leader.getUsername();
            String imageName = image.getImageName();

            Integer polarity = tevInformation.getPolarity();
            String polarityName = polarity == 1 ? "正极性" : polarity == 0 ? "负极性" : "-";

            sb.append("【消息内容】\n");
            sb.append("业务类型：局部TEV数据\n");
            sb.append("用户名称：" + username + "\n");
            sb.append("领导名称：" + leaderName + "\n");
            sb.append("设备名称：" + device.getDeviceName() + "\n");
            sb.append("设备类型：" + deviceTypeName + "\n");
            sb.append("上传时图像名称：" + imageName + "\n");
            sb.append("局部TEV有效值：" + tevInformation.getQuasiPeakValue() + "mV\n");
            sb.append("局部TEV最大值：" + tevInformation.getMaxValue() + "mV\n");
            sb.append("局部TEV最小值：" + tevInformation.getMinValue() + "mV\n");
            sb.append("局部TEV发生时间：" + sdf.format(tevInformation.getOccurrenceTime()) + "\n");
            sb.append("局部TEV最大值：" + tevInformation.getMaxValue() + "\n");
            sb.append("极性：" + polarityName + "\n");
            sb.append("局部TEV频率：" + tevInformation.getRepetitionRate() + "hZ\n");
            sb.append("工单审核状态：失败\n");
            sb.append("工单审核结束时间：" + sdf.format(workOrder.getFailTime()) + "\n");
            sb.append("数据创建时间：" + sdf.format(tevInformation.getCreateTime()) + "\n");
            sb.append("该数据审核未通过，请关注相关数据");
            msgContent = sb.toString();
        }

        DingUtils.sendDing(messageUrl, msgContent);
        msgContent = msgContent.replaceAll("\n", "<br>");

        this.save(MessageWarningRecord.builder()
                .messageWarningId(messageWarningId)
                .messageBizId(messageBizId)
                .messageType(messageType)
                .messageContent(msgContent)
                .build()
        );
    }
}




