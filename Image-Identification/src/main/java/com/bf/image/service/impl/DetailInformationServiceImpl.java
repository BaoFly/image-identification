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
import com.bf.image.service.DetailInformationService;
import com.bf.image.mapper.DetailInformationMapper;
import com.bf.image.utils.TimeUtil;
import com.bf.image.utils.UUIDUtil;
import com.bf.image.vo.DetailInformationVo;
import org.apache.commons.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.BeanUtils;
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
    private UserInformationMapper userMapper;

    @Autowired
    private DeviceInformationMapper deviceMapper;

    @Autowired
    private ImageInformationMapper imageMapper;

    @Autowired
    private DetailInformationMapper detailMapper;

    @Override
    @Transactional
    public void uploadInfo(DetailInformation detailInformation, String fullUrl) {
        // 拿到对应的封装的实体类
        Object imageObj = detailInformation.getImageObj();
        UserInformation user = detailInformation.getUser();
        UserInformation workLeader = detailInformation.getWorkLeader();
        DeviceInformation device = detailInformation.getDevice();

        Long detailId = UUIDUtil.generateUUID();
        detailInformation.setDetailId(detailId);

        Date currentTime = TimeUtil.getCurrentTime();
        detailInformation.setCreateTime(currentTime);
        detailInformation.setCreateTime(currentTime);

        // 调用封装好的方法，直接返回对应image对象
        ImageInformation imageInfo = loadLocal(imageObj, fullUrl, user.getUsername());
        if (!Objects.isNull(imageInfo)) {
            detailInformation.setImage(imageInfo);
            // 先将该图片信息插入数据库
            imageMapper.insert(imageInfo);
        }

        // 拿到User对应的信息
        UserInformation userInfo = userMapper.selectOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUsername, user.getUsername()));
        if (Objects.isNull(userInfo)) {
            throw new CustomException("不存在该用户");
        } else {
            detailInformation.setUser(userInfo);
        }

        // 拿到WorkLeader对应的信息
        UserInformation leaderInfo = userMapper.selectOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUsername, workLeader.getUsername()));
        if (Objects.isNull(leaderInfo)) {
            throw new CustomException("不存在该leader");
        } else {
            detailInformation.setWorkLeader(leaderInfo);
        }

        DeviceInformation deviceInfo = deviceMapper.selectOne(new LambdaQueryWrapper<DeviceInformation>().eq(DeviceInformation::getDeviceId, device.getDeviceId()));
        if (Objects.isNull(deviceInfo)) {
            throw new CustomException("不存在该设备ID");
        } else {
            detailInformation.setDevice(deviceInfo);
        }

        detailMapper.insertBySelf(detailInformation);

    }

    private ImageInformation loadLocal(Object imageObj, String fullUrl, String username) {
        if (Objects.isNull(imageObj)) {
            return null;
        }
        // 创建存储图片的目录（如果不存在）
        String userImageStorageDir = fullUrl + CommonConstant.IMAGE_DIR + "\\" + username;
        File imageDir = new File(userImageStorageDir);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        Long imageId = UUIDUtil.generateUUID();
        Date currentTime = TimeUtil.getCurrentTime();
        // 直接用日期作为图片的名字
        Long imageStorageName = TimeUtil.getCurrentTime().getTime();

        if (imageObj instanceof String) {
            ImageInformation imageInfo = new ImageInformation();
            // 转成字符串类型
            String imageStr = (String) imageObj;
            if (!Base64.isBase64(imageStr)) {
                throw new CustomException("无效的Base64编码");
            }
            // 解析 Base64 字符串为字节数组
            byte[] imageBytes = Base64.decodeBase64(imageStr.replace("data:image/jpeg;base64,", ""));
            for (int i = 0; i < imageBytes.length; ++i) {
                if (imageBytes[i] < 0) {
                    imageBytes[i] += 256;
                }
            }
            // 拿到对应的图片字节长度
            long imageSize = imageBytes.length;
            // 校验图片大小是否小于10M
            if (imageSize > 10 * 1024 * 1024) {
                throw new CustomException(CommonConstant.FAIL_CODE, CommonConstant.IMAGE_SIZE_EXCEED);
            }

            // 开始上传图片至服务器的ContextPath + all-images + username 路径下
            FileOutputStream fos = null;

            Path filePath = Paths.get(userImageStorageDir, String.valueOf(imageStorageName));
            Path resolvedPath = filePath.resolveSibling(imageStorageName + CommonConstant.IMAGE_TYPE);
            try {
                fos = new FileOutputStream(resolvedPath.toFile());
                fos.write(imageBytes);
                fos.flush();
            } catch (IOException e) {
                throw new CustomException("上传图片IO异常");
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        throw new CustomException("IO关闭时异常");
                    }
                }
            }

            imageInfo.setImageId(imageId);
            imageInfo.setImageSize(imageSize);
            imageInfo.setImageName("");
            imageInfo.setImagePath(userImageStorageDir);
            imageInfo.setStorageName(imageStorageName.toString());
            imageInfo.setCreateTime(currentTime);
            imageInfo.setUpdateTime(currentTime);
            imageInfo.setCreatorName(username);

            return imageInfo;
        }

//        if (imageObj instanceof MultipartFile) {
//            MultipartFile imageFile = (MultipartFile) imageObj;
//            ImageInformation imageInfo = new ImageInformation();
//            byte[] imageBytes = null;
//            try {
//                imageBytes = imageFile.getBytes();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            String imageOriginalName = imageFile.getOriginalFilename();
//            Long imageSize = imageFile.getSize();
//            if (imageSize > 10 * 1024 * 1024) {
//                throw new CustomException(CommonConstant.FAIL_CODE, CommonConstant.IMAGE_SIZE_EXCEED);
//            }
//            // 上传图片
//            try {
//                imageFile.transferTo(new File(userImageStorageDir + imageStorageName));
//            } catch (IOException e) {
//                throw new CustomException("MultipartFile上传异常");
//            }
//
//            imageInfo.setImageId(imageId);
//            imageInfo.setImageName(imageOriginalName);
//            imageInfo.setImagePath(userImageStorageDir);
//            imageInfo.setStorageName(imageStorageName.toString());
//            imageInfo.setImageSize(imageSize);
//            imageInfo.setCreateTime(currentTime);
//            imageInfo.setUpdateTime(currentTime);
//            imageInfo.setCreatorName(username);
//
//            return imageInfo;
//        }

        return null;
    }

    @Override
    public DetailInformation getNewestInfo(DetailInformation detailInformation) {
        Long deviceId = detailInformation.getDevice().getDeviceId();
        if (Objects.isNull(deviceId)) {
            return null;
        }
        DetailInformation detailInfo = detailMapper.selectByDeviceId(deviceId);
        if (Objects.isNull(detailInfo)) {
            throw new CustomException(CommonConstant.FAIL_CODE, "该设备下无信息");
        }
        Long imageId = detailInfo.getImage().getImageId();
        ImageInformation imageInfo = imageMapper.selectOne(new LambdaQueryWrapper<ImageInformation>().eq(ImageInformation::getImageId, imageId));
        if (Objects.isNull(imageInfo)) {
            throw new CustomException(CommonConstant.FAIL_CODE, "无图片信息");
        }
        String imagePath = imageInfo.getImagePath();
        String storageName = imageInfo.getStorageName();
        if (StringUtils.isBlank(imagePath) || StringUtils.isBlank(storageName)) {
            throw new CustomException(CommonConstant.FAIL_CODE, "图片路径异常");
        }
        String fileFullName = imagePath + "\\" + storageName + CommonConstant.IMAGE_TYPE;

        // 读取图片文件
        Path imageFilePath = Paths.get(fileFullName);
        byte[] imageBytes = new byte[0];
        try {
            imageBytes = Files.readAllBytes(imageFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 将图片字节数组转换为Base64编码
        String base64String = Base64.encodeBase64String(imageBytes);
        detailInformation.setImageObj(base64String);
        detailInformation.setImage(imageInfo);
        return detailInformation;
    }

    @Override
    public List<DetailInformation> getDetailInfoByCondition(DetailInformationVo detailInfo) {
        List<DetailInformation> allDetailInfo = detailMapper.selectAll();
        if (Objects.isNull(allDetailInfo)) {
            return null;
        }
        allDetailInfo.stream().forEach(detailInformation -> {
            UserInformation userInformation = userMapper.selectById(detailInformation.getUser().getUserId());
            DeviceInformation deviceInformation = deviceMapper.selectById(detailInformation.getDevice().getDeviceId());
            ImageInformation imageInformation = null;
            if (detailInformation.getImage().getImageId() != null) {
                imageInformation = imageMapper.selectById(detailInformation.getImage().getImageId());
            }
            detailInformation.setUser(userInformation);
            detailInformation.setDevice(deviceInformation);
            detailInformation.setImage(imageInformation);
        });

//        String username = detailInfo.getUser().getUsername();
//        Long deviceId = detailInfo.getDevice().getDeviceId();
//        Integer deviceType = detailInfo.getDevice().getDeviceType();
//        String powerSupplyStation = detailInfo.getPowerSupplyStation();
//        Double feeder = detailInfo.getFeeder();
//        String substationName = detailInfo.getSubstationName();
//        String inspectionTeam = detailInfo.getInspectionTeam();
//        Long leaderId = detailInfo.getWorkLeader().getUserId();
//        String deviceName = detailInfo.getDevice().getDeviceName();
//        Double maxTemp = detailInfo.getMaxTemp();
//        Date createTime = detailInfo.getCreateTime();

//
//        List<DetailInformation> filterList = allDetailInfo.stream()
//                .filter(detail -> (detailInfo.getUser().getUsername() == null || detail.getUser().getUsername().equals(detailInfo.getUser().getUsername())))
//                .filter(detail -> (detailInfo.getDeviceId() == null || detail.getDevice().getDeviceId().equals(detailInfo.getDeviceId())))
//                .filter(detail -> (detailInfo.getDeviceType() == null || detail.getDevice().getDeviceType().equals(detailInfo.getDeviceType())))
//                .filter(detail -> (detailInfo.getPowerSupplyStation() == null || detail.getPowerSupplyStation().equals(detail.getPowerSupplyStation())))
//                .filter(detail -> (detailInfo.getFeeder() == null || detail.getFeeder().equals(detail.getFeeder())))
//                .filter(detail -> (detailInfo.getSubstationName() == null || detail.getSubstationName().equals(detail.getSubstationName())))
//                .filter(detail -> (detailInfo.getInspectionTeam() == null || detail.getInspectionTeam().equals(detail.getInspectionTeam())))
//                .filter(detail -> (detailInfo.getWorkLeaderId() == null || detail.getWorkLeader().getUserId().equals(detailInfo.getWorkLeaderId())))
//                .filter(detail -> (detailInfo.getDeviceName() == null || detail.getDevice().getDeviceName().equals(detailInfo.getDeviceName())))
//                .filter(detail -> (detailInfo.getMaxTemp() == null || detail.getMaxTemp() > detailInfo.getMaxTemp()))
//                .filter(detail -> (detailInfo.getCreateTime() == null || detail.getCreateTime().after(detailInfo.getCreateTime())))
//                .collect(Collectors.toList());

        return null;
    }

    @Override
    public DetailInformation convert(DetailInformationVo detailInformationVo) {
        DetailInformation detailInformation = new DetailInformation();
        DeviceInformation deviceInformation = new DeviceInformation();

        UserInformation userInformation = new UserInformation();
        ImageInformation imageInformation = new ImageInformation();

//        deviceInformation.setDeviceId(detailInformationVo.getDeviceId());
//        deviceInformation.setDeviceName(detailInformationVo.getDeviceName());
//        deviceInformation.setDeviceType(detailInformationVo.getDeviceType());
//
//        userInformation.setUserId(detailInformationVo.getUserId());
//        userInformation.setUsername(detailInformationVo.getUsername());
//
//        imageInformation.setImageId(detailInformationVo.getImageId());
//        imageInformation.setImageName(detailInformationVo.getImageName());
//        imageInformation.setImagePath(detailInformationVo.getImagePath());
//        imageInformation.setImageSize(detailInformationVo.getImageSize());
//        imageInformation.setCreatorName(detailInformationVo.getCreatorName());
//        imageInformation.setStorageName(detailInformationVo.getStorageName());
//
//
//        BeanUtils.copyProperties(detailInformationVo, detailInformation);
//
//        detailInformation.setUser(userInformation);
//        detailInformation.setDevice(deviceInformation);
//        detailInformation.setImage(imageInformation);

        return detailInformation;
    }

    @Override
    public List<DetailInformation> getAllRecord() {
        return detailMapper.selectList(null);
    }

    @Override
    public IPage<DetailInformation> selectPage(DetailInformationVo detailInformationVo) {
        Integer pageSize = detailInformationVo.getPageSize();
        Page<DetailInformation> page = new Page<>(detailInformationVo.getCurrent(), pageSize);
        IPage<DetailInformation> iPage = detailMapper.queryPage(page, detailInformationVo);
        Long count = detailMapper.selectAll().stream().collect(Collectors.counting());
        long pages = 0;
        if (count % pageSize == 0) {
            pages = count / pageSize;
        } else {
            pages = count / pageSize + 1;
        }
        iPage.setPages(pages);
        iPage.setTotal(count);
        return iPage;
    }

}




