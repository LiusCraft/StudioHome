package org.liuscraft.gzs.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class Slide {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String text;
    private String btnName;
    private String url;
    private String imgUrl;
    private String target;
}
