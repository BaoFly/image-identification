package com.bf.image.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 
 * @TableName tev_information
 */
@TableName(value ="tev_information")
@Data
public class TevInformation implements Serializable {
    /**
     * 表主键ID
     */
    @TableId(value = "tev_id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tevId;

    /**
     * 局部TEV有效值
     */
    @TableField(value = "quasi_peak_value")
    private Double quasiPeakValue;

    /**
     * 局部TEV最大值
     */
    @TableField(value = "max_value")
    private Double maxValue;

    /**
     * 局部TEV最小值
     */
    @TableField(value = "min_value")
    private Double minValue;

    /**
     * 局部TEV发生时间
     */
    @TableField(value = "occurrence_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date occurrenceTime;

    /**
     * 极性 1：正极性 0：负极性
     */
    @TableField(value = "polarity")
    private Integer polarity;

    /**
     * 局部TEV频率
     */
    @TableField(value = "repetition_rate")
    private Integer repetitionRate;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 备注
     */
    @TableField(value = "memo")
    private String memo;

    /**
     * 图片ID
     */
    @TableField(value = "image_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long imageId;

    /**
     * 设备ID
     */
    @TableField(value = "device_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deviceId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 领导ID
     */
    @TableField(value = "leader_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long leaderId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TevInformation other = (TevInformation) that;
        return (this.getTevId() == null ? other.getTevId() == null : this.getTevId().equals(other.getTevId()))
            && (this.getQuasiPeakValue() == null ? other.getQuasiPeakValue() == null : this.getQuasiPeakValue().equals(other.getQuasiPeakValue()))
            && (this.getMaxValue() == null ? other.getMaxValue() == null : this.getMaxValue().equals(other.getMaxValue()))
            && (this.getMinValue() == null ? other.getMinValue() == null : this.getMinValue().equals(other.getMinValue()))
            && (this.getOccurrenceTime() == null ? other.getOccurrenceTime() == null : this.getOccurrenceTime().equals(other.getOccurrenceTime()))
            && (this.getPolarity() == null ? other.getPolarity() == null : this.getPolarity().equals(other.getPolarity()))
            && (this.getRepetitionRate() == null ? other.getRepetitionRate() == null : this.getRepetitionRate().equals(other.getRepetitionRate()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getMemo() == null ? other.getMemo() == null : this.getMemo().equals(other.getMemo()))
            && (this.getImageId() == null ? other.getImageId() == null : this.getImageId().equals(other.getImageId()))
            && (this.getDeviceId() == null ? other.getDeviceId() == null : this.getDeviceId().equals(other.getDeviceId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getLeaderId() == null ? other.getLeaderId() == null : this.getLeaderId().equals(other.getLeaderId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTevId() == null) ? 0 : getTevId().hashCode());
        result = prime * result + ((getQuasiPeakValue() == null) ? 0 : getQuasiPeakValue().hashCode());
        result = prime * result + ((getMaxValue() == null) ? 0 : getMaxValue().hashCode());
        result = prime * result + ((getMinValue() == null) ? 0 : getMinValue().hashCode());
        result = prime * result + ((getOccurrenceTime() == null) ? 0 : getOccurrenceTime().hashCode());
        result = prime * result + ((getPolarity() == null) ? 0 : getPolarity().hashCode());
        result = prime * result + ((getRepetitionRate() == null) ? 0 : getRepetitionRate().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getMemo() == null) ? 0 : getMemo().hashCode());
        result = prime * result + ((getImageId() == null) ? 0 : getImageId().hashCode());
        result = prime * result + ((getDeviceId() == null) ? 0 : getDeviceId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getLeaderId() == null) ? 0 : getLeaderId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", tevId=").append(tevId);
        sb.append(", quasiPeakValue=").append(quasiPeakValue);
        sb.append(", maxValue=").append(maxValue);
        sb.append(", minValue=").append(minValue);
        sb.append(", occurrenceTime=").append(occurrenceTime);
        sb.append(", polarity=").append(polarity);
        sb.append(", repetitionRate=").append(repetitionRate);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", memo=").append(memo);
        sb.append(", imageId=").append(imageId);
        sb.append(", deviceId=").append(deviceId);
        sb.append(", userId=").append(userId);
        sb.append(", leaderId=").append(leaderId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}