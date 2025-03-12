package com.bf.image.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bf.image.config.MinIOConfig;
import com.bf.image.domin.vo.FileVo;
import com.bf.image.entity.FileLatticeData;
import com.bf.image.entity.ImageInformation;
import com.bf.image.mapper.FileLatticeDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author BorjaHu_xmagwy0
* @description 针对表【file_lattice_data(点阵数据表)】的数据库操作Service实现
* @createDate 2025-03-12 16:07:04
*/
@Service
public class FileLatticeDataServiceImpl extends ServiceImpl<FileLatticeDataMapper, FileLatticeData>
    implements IService<FileLatticeData> {

    private static final Logger log = LoggerFactory.getLogger(FileLatticeDataServiceImpl.class);

    @Autowired
    private MinIOConfig minIOConfig;

    @Autowired
    private MinIOUServiceImpl minIOUService;

    public void saveTxtByFileVo(List<FileVo> fileVos, String dataId, Integer type) {
        log.info("保存上传的Txt文件：【{}】", JSONObject.toJSONString(fileVos));

        String bucketName = minIOConfig.getBucketName();
        List<FileLatticeData> fileLatticeDataList = new ArrayList<>();

        for (FileVo fileVo : fileVos) {
            FileLatticeData fileLatticeData = FileLatticeData.builder()
                    .detailId(Long.valueOf(dataId))
                    .fixType(type)
                    .bucketName(bucketName)
                    .fileSize(fileVo.getSize())
                    .fileStorageName(fileVo.getFileName())
                    .fileOriginalName(fileVo.getOriginalName())
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();

            fileLatticeDataList.add(fileLatticeData);
        }

        this.saveBatch(fileLatticeDataList);
    }
}




