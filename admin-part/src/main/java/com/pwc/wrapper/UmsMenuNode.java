package com.pwc.wrapper;

import com.pwc.model.UmsMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UmsMenuNode extends UmsMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<UmsMenuNode> children;
}
