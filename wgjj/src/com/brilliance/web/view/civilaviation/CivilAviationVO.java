package com.brilliance.web.view.civilaviation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import com.brilliance.base.model.internetModel.NoteCivilReply;
import com.brilliance.base.model.internetModel.NoteFiles;
import com.brilliance.base.model.internetModel.NotePlanFlight;
import com.brilliance.base.model.internetModel.NotePlanInfo;

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
