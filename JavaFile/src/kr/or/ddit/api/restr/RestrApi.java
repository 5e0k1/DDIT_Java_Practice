package kr.or.ddit.api.restr;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.or.ddit.vo.RestrVo;

public class RestrApi {
	String url = "http://apis.data.go.kr/6300000/openapi2022/restrnt/getrestrnt";
	String serviceKey ="z5XDXN23w4UGvjSkQaGqg1jn6AXmhjVSwFNAa76AEpmxixj%2BWwIk6UVi7BEL%2BmXhbuPi%2BEXA0aclFqOs5VoIpw%3D%3D";

	public static void main(String[] args) {
		RestrApi obj = new RestrApi();
		obj.process();
	}
	
	RestrDao restrDao = RestrDao.getInstance();
	
	public void process() {
		String html = getHtml(1);
		int totalCount = totalCount(html);
		for(int i=1; i<=Math.ceil(totalCount/10.0);i++) {
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			List<RestrVo> list = getList(getHtml(i));
			for(RestrVo restrVo : list) {
				List<Object> param = new ArrayList<>();
				
				param.add(restrVo.getRestrnt_nm());
				param.add(restrVo.getRestrnt_zip());
				param.add(restrVo.getRestrnt_addr());
				param.add(restrVo.getRestrnt_dtl_addr());
				param.add(restrVo.getRestrnt_inqrtel());
				param.add(restrVo.getRprs_fod_name());
				param.add(restrVo.getRprs_fod_price());
				param.add(restrVo.getSals_time());
				param.add(restrVo.getHldy_guid());
				param.add(restrVo.getRestrnt_summ());
				param.add(restrVo.getMap_lat());
				param.add(restrVo.getMap_lot());
				
				restrDao.insertRestr(param);
			}
		}
	}
	
	public List<RestrVo> getList(String html){
		
		List<RestrVo> list = new ArrayList<>();
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject root = (JSONObject) parser.parse(html);
			JSONObject response = (JSONObject) root.get("response");
			JSONObject body = (JSONObject) response.get("body");
			JSONArray restrArr = (JSONArray) body.get("items"); 
			
			for(int i=0; i<restrArr.size();i++){
				RestrVo restrVo = new RestrVo();
				// 값 꺼내기
				JSONObject restr = (JSONObject) restrArr.get(i);
				String restrntNm 		= (String) restr.get("restrntNm");
				String restrntZip 		= (String) restr.get("restrntZip");
				String restrntAddr 		= (String) restr.get("restrntAddr");
				String restrntDtlAddr 	= (String) restr.get("restrntDtlAddr");
				String restrntInqrTel 	= (String) restr.get("restrntInqrTel");
				String rprs_fod_name	= null;
				String rprs_fod_price	= null;
				String rprsFod 			= (String) restr.get("rprsFod");
				if(rprsFod.split("/").length==1) {
					rprs_fod_name 	= rprsFod.split("/")[0].trim();
				}
				
				if(rprsFod.split("/").length==2) {
					rprs_fod_name 	= rprsFod.split("/")[0].trim();
					rprs_fod_price 	= rprsFod.split("/")[1].trim();
				}
				
				String salsTime 		= (String) restr.get("salsTime");
				String hldyGuid 		= (String) restr.get("hldyGuid");
				String restrntSumm 		= (String) restr.get("restrntSumm");
				String mapLat 			= (String) restr.get("mapLat");
				String mapLot 			= (String) restr.get("mapLot");
				
				// Vo에 값 세팅
				// 번호세팅해야함 121개 정보 한번에 쭉갈거면 i+1, 아니면 전역변수?
				// 오라클 단에서 처리해도됨->쿼리문작성시 번호부여
//				restrVo.setRestrnt_no(i+1);
				restrVo.setRestrnt_nm(restrntNm);
				restrVo.setRestrnt_zip(restrntZip);
				restrVo.setRestrnt_addr(restrntAddr);
				restrVo.setRestrnt_dtl_addr(restrntDtlAddr);
				restrVo.setRestrnt_inqrtel(restrntInqrTel);
				restrVo.setRprs_fod_name(rprs_fod_name);
				restrVo.setRprs_fod_price(rprs_fod_price);
				restrVo.setSals_time(salsTime);
				restrVo.setHldy_guid(hldyGuid);
				restrVo.setRestrnt_summ(restrntSumm);
				restrVo.setMap_lat(mapLat);
				restrVo.setMap_lot(mapLot);
				
				// 리스트에 Vo추가
				list.add(restrVo);
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	public int totalCount(String html) {
		
		JSONParser parser = new JSONParser();
		int totalCount =0;
		try {
			JSONObject root = (JSONObject) parser.parse(html);
			JSONObject response = (JSONObject) root.get("response");
			JSONObject body = (JSONObject) response.get("body");
			totalCount = (int)(long) body.get("totalCount");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return totalCount;
	}
	
	
	public String getHtml(int pageNoInt) {
		String pageNo = pageNoInt+"";
		String numOfRows = "10";
		String fullUrl =url+"?serviceKey="+serviceKey+"&pageNo="+pageNo+"&numOfRows="+numOfRows;
		
		String html =ApiUtil.httpConnect(fullUrl);
		
		return html;
	}
}
