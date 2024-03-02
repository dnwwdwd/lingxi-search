package com.hjj.lingxisearch.model.dto.picture;

import com.hjj.lingxisearch.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class PictureQueryRequest extends PageRequest implements Serializable {
    String searchText;
}
