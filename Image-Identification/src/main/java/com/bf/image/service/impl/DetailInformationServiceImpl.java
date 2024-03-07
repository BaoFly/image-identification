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
        String username = detailInformation.getUsername();
        String workLeaderName = detailInformation.getWorkLeaderName();
        DeviceInformation device = detailInformation.getDevice();

        Long detailId = UUIDUtil.generateUUID();
        detailInformation.setDetailId(detailId);

        Date currentTime = TimeUtil.getCurrentTime();
        detailInformation.setCreateTime(currentTime);
        detailInformation.setCreateTime(currentTime);

        // 调用封装好的方法，直接返回对应image对象
        ImageInformation imageInfo = loadLocal(imageObj, fullUrl, username);
        if (!Objects.isNull(imageInfo)) {
            detailInformation.setImage(imageInfo);
            // 先将该图片信息插入数据库
            imageMapper.insert(imageInfo);
        }

        // 拿到User对应的信息
        UserInformation userInfo = userMapper.selectOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUsername, username));
        if (Objects.isNull(userInfo)) {
            throw new CustomException("不存在该用户");
        } else {
            detailInformation.setUsername(userInfo.getUsername());
        }

        // 拿到WorkLeader对应的信息
        UserInformation leaderInfo = userMapper.selectOne(new LambdaQueryWrapper<UserInformation>().eq(UserInformation::getUsername, workLeaderName));
        if (Objects.isNull(leaderInfo)) {
            throw new CustomException("不存在该leader");
        } else {
            detailInformation.setWorkLeaderName(leaderInfo.getUsername());
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
        } else {
            return null;
        }
    }

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
        return iPage;
    }

}




