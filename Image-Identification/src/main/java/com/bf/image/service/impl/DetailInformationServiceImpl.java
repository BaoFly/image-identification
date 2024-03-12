package com.bf.image.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.constant.CommonConstant;
import com.bf.image.exception.CustomException;
import com.bf.image.mapper.DeviceInformationMapper;
import com.bf.image.mapper.ImageInformationMapper;
import com.bf.image.mapper.UserInformationMapper;
import com.bf.image.pojo.DetailInformation;
import com.bf.image.pojo.DeviceInformation;
import com.bf.image.pojo.ImageInformation;
import com.bf.image.pojo.UserInformation;
import com.bf.image.service.*;
import com.bf.image.mapper.DetailInformationMapper;
import com.bf.image.utils.TimeUtil;
import com.bf.image.utils.UUIDUtil;
import com.bf.image.vo.DetailInformationVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author Administrator
* @description 针对表【deatil_information】的数据库操作Service实现
* @createDate 2023-10-14 18:40:13
*/
@Service
public class DetailInformationServiceImpl extends ServiceImpl<DetailInformationMapper, DetailInformation>
    implements DetailInformationService {

    @Autowired
    private UserInformationService userService;

    @Autowired
    private DeviceInformationService deviceService;

    @Autowired
    private ImageInformationService imageService;

    @Autowired
    private DetailInformationMapper detailMapper;

    @Autowired
    private MinIOUService minIOUService;

    @Override
    public IPage<DetailInformation> pageVo(DetailInformationVo detailInformationVo) {
        Integer pageSize = detailInformationVo.getPageSize();
        Integer offset = (detailInformationVo.getCurrent() - 1) * pageSize;
        System.out.println(offset);
        Page<DetailInformation> page = new Page<>();
        page.setCurrent(offset);
        page.setSize(pageSize);
        IPage<DetailInformation> iPage = detailMapper.queryPage(page, detailInformationVo);
        Long total = detailMapper.selectCount(detailInformationVo);
        Long pages = 0L;
        if (total % pageSize == 0 | total == 0) {
            pages = total % pageSize;
        } else {
            pages = total / pageSize + 1;
        }
        iPage.setTotal(total);
        iPage.setPages(pages);

        List<DetailInformation> records = iPage.getRecords();
        for (DetailInformation record : records) {
            ImageInformation image = record.getImage();
            String bucketName = image.getBucketName();
            String storageName = image.getStorageName();
            String previewUrl = minIOUService.getPreviewUrl(storageName, bucketName);
            record.setPreviewUrl(previewUrl);
        }

        return iPage;
    }

    @Override
    @Transactional
    public void saveRecord(DetailInformationVo detailInformationVo) {
        String username = detailInformationVo.getUsername();
        String workLeaderName = detailInformationVo.getWorkLeaderName();
        UserInformation user = userService.getOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUsername, username));
        UserInformation workLeader = userService.getOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUsername, workLeaderName));
        if (Objects.isNull(user) || Objects.isNull(workLeader)) {
            throw new CustomException(CommonConstant.NO_EXIST_USER);
        }

        DeviceInformation device = detailInformationVo.getDevice();
        Long deviceId = UUIDUtil.generateUUID();
        device.setDeviceId(deviceId);
        deviceService.save(device);

        Long detailId = UUIDUtil.generateUUID();
        detailInformationVo.setDetailId(detailId);
        detailInformationVo.setDevice(device);
        detailMapper.saveRecord(detailInformationVo);
    }

}




