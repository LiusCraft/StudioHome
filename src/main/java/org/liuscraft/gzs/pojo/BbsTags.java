package org.liuscraft.gzs.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BbsTags {
    private Integer id;
    private String name;
    private String slug;
    private String description;
}
