package com.uav.web.view.civilaviation;

import com.uav.base.model.internetModel.NoteCivilReply;
import com.uav.base.model.internetModel.NoteFiles;
import com.uav.base.model.internetModel.NotePlanFlight;
import com.uav.base.model.internetModel.NotePlanInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CivilAviationVO implements Serializable {
    //民航回复信息
    private NoteCivilReply noteCivilReply;

    //照会文书列表
    private List<NoteFiles>  noteFiles;

    //飞行计划信息表
    private List<NotePlanFlight> planFlights;

    //飞机信息与照会信息
    private NotePlanInfo planInfo;
}
