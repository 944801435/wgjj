package com.uav.base.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

/**
 * @description: 经纬度转度分秒
 * @author: huangqg
 * @create: 2018-09-18 13:00
 **/
public class JwdToDfm {

	// 地理坐标系转换成度分秒
	private static String jwd_to_dfm(String jwdStr) {
		if (StringUtils.isBlank(jwdStr)) {
			return "";
		}
		float jwd = Float.parseFloat(jwdStr);
		int du = (int) jwd;
		float fm = jwd - du;
		float ff = fm * 60;
		int fen = (int) ff;
		float m = (ff - fen) * 60;
		BigDecimal bd = new BigDecimal(m).setScale(0, BigDecimal.ROUND_HALF_UP);
		int miao = Integer.parseInt(bd.toString());

		String fenStr = fen < 10 ? ("0" + fen) : (fen + "");
		String miaoStr = miao < 10 ? ("0" + miao) : (miao + "");
		String dfm = du + "°" + fenStr + "′" + miaoStr + "″";
		return dfm;
	}

	/**
	 * 十进制经度转成度分秒经度
	 * 
	 * @Title: jd_to_dmf
	 * @author maqian
	 * @date 2020年4月20日 下午2:08:47
	 * @param jdStr
	 * @return
	 */
	public static String jd_to_dfm(String jdStr) {
		if (StringUtils.isBlank(jdStr)) {
			return "";
		}
		String firstStr = "";
		float jd = Float.parseFloat(jdStr);
		if (jd < 0) {
			firstStr = "W";
			jd = jd * -1;
		} else {
			firstStr = "E";
		}
		return firstStr + jwd_to_dfm(jdStr);
	}

	/**
	 * 十进制纬度转成度分秒纬度
	 * 
	 * @Title: wd_to_dmf
	 * @author maqian
	 * @date 2020年4月20日 下午2:11:47
	 * @param wdStr
	 * @return
	 */
	public static String wd_to_dfm(String wdStr) {
		if (StringUtils.isBlank(wdStr)) {
			return "";
		}
		String firstStr = "";
		float wd = Float.parseFloat(wdStr);
		if (wd < 0) {
			firstStr = "S";
			wd = wd * -1;
		} else {
			firstStr = "N";
		}
		return firstStr + jwd_to_dfm(wdStr);
	}

	/**
	 * 简易版度分秒转经纬度
	 * 
	 * @Title: dfm_to_jwd
	 * @author maqian
	 * @date 2020年6月1日 下午1:25:14
	 * @param dfm
	 * @return
	 */
	public static String dfm_to_jwd(String dfm) {
		if (dfm.indexOf("E") != -1 || dfm.indexOf("N") != -1 || dfm.indexOf("W") != -1 || dfm.indexOf("S") != -1) {
			dfm = dfm.substring(1);
		}
		String duStr = dfm.split("°")[0];
		String fenStr = dfm.split("°")[1].split("′")[0];
		String miaoStr = dfm.split("°")[1].split("′")[1].split("″")[0];
		String firstStr = "";
		int du = Integer.parseInt(duStr);
		int fen = Integer.parseInt(fenStr);
		double miao = Double.parseDouble(miaoStr);
		double mf = miao / 60.0;
		double ff = fen + mf;
		double fm = ff / 60.0;
		String jwd = firstStr + (du + fm);
		return jwd;
	}

	public static void main(String[] args) {
//		String jwdStr = "116.232112322";
//		System.out.println(jwd_to_dfm(jwdStr));
//		System.out.println(dfm_to_jwd("E116°13′56″"));
//		
//		System.out.println(jwd_to_dfm("110.662545"));
//		System.out.println(jwd_to_dfm(″19.6086247″));
		
		
		String s = "19°02′51″,110°33′12.11″-19°35′54″,109°15′28″-18°16′55″,109°27′33″-18°13′05″,109°30′58″-18°13′39″,109°21′44″-18°14′42″,109°29′32″-18°18′40″,109°45′47″-18°20′48″,109°08′33″-18°19′24″,109°43′56″-18°19′12″,109°41′16″-18°33′11″,110°07′02″-18°40′08″,110°17′16″-18°21′09″,109°44′10″-18°17′37″,109°12′44″-18°18′04″,109°20′20″-18°21′17″,109°44′49″-18°27′06″,109°40′07″-18°21′56″,109°45′06″-18°22′23″,109°45′19″-18°23′06″,109°59′16″-18°20′13″,109°44′23″-18°35′03″,110°10′39″-18°24′27″,109°58′18″-18°34′19″,110°12′08″-18°36′52″,110°12′28″-18°33′40″,110°6′21″-18°34′5″,110°6′51″-18°15′19″,109°28′49″-18°23′27″,109°48′58″-18°24′04″,109°57′02″-18°17′24″,109°23′18″";
		String[] dfms = s.split("-");
		for(String dfm : dfms){
			String jd = dfm.split(",")[1];
			String wd = dfm.split(",")[0];
			System.out.println(dfm_to_jwd(jd)+"  "+dfm_to_jwd(wd));
		}
	}

}
