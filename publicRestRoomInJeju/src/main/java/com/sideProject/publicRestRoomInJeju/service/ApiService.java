package com.sideProject.publicRestRoomInJeju.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideProject.publicRestRoomInJeju.vo.ToiletInfoVo;

@Service
public class ApiService {

	
	public List<ToiletInfoVo> getToiletList() throws IOException, ParseException {
		
		HttpsURLConnection conn = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		
		List<ToiletInfoVo> resultList = new ArrayList<>();
		
		// OpenAPI URL 및 서비스키
		String endPointUrl = "https://apis.data.go.kr/6510000/publicToiletService/getPublicToiletInfoList";
		String serviceKey = "wrfUugC5ho0Mi1xMSO5FuHxwwFOifMq7kNveO1Pj%2Bqmb6BfLDX0DTKZsFdMTevSxJ%2BsRFrR5gPlHWWQZIlEt7w%3D%3D";
		int pageNo = 1;
		
		// 행 수가 토탈 카운트와 같도록 한다. (1page에 모든 데이터가 나오게~)
		Object totalData; //numOfRows에 들어갈 변수 (totalCount = numOfRows)
		
		String rqstURL = endPointUrl + "?serviceKey=" + serviceKey + "&" + "pageNo=" + pageNo + "&numOfRows=415";
						// + "&" + "numOfRows=" + totalData;
		
		
		try {
			
				URL url = new URL(rqstURL);
				conn = (HttpsURLConnection) url.openConnection();
				
				int respCode = conn.getResponseCode();
				System.out.println("응답코드 : " + respCode + "\n" + "응답메시지 : " + conn.getResponseMessage() + "\n");
				
				if(respCode != HttpsURLConnection.HTTP_OK) {
					throw new RuntimeException("HTTP ERROR CODE : " + conn.getResponseMessage());
				}
				
				
				inputStream = conn.getInputStream();
				reader = new BufferedReader(new InputStreamReader(inputStream));
				
				StringBuilder responseBuilder = new StringBuilder();
				String line = "";
				while( (line = reader.readLine()) != null ) {
					responseBuilder.append(line);
				}
				
				String responseJson = responseBuilder.toString();
				//System.out.println("\n responseJson : \n" + responseJson);
				
				
				// JSON 데이터를 객체로 변환
				JSONParser parser = new JSONParser();
				JSONObject parse = (JSONObject) parser.parse(responseJson);
				JSONObject response = (JSONObject) parse.get("response");
				JSONObject body = (JSONObject) response.get("body");
				JSONObject items = (JSONObject) body.get("items");
				JSONArray item = (JSONArray) items.get("item");
				
				totalData = body.get("totalCount");
//				System.out.println(":::토탈데이터 데이터타입::: " + totalData);
//				System.out.println(totalData.getClass().getName()); //토탈데이터는 long타입임
				
				// item에서 하나씩 JSONObject로 cast해서 사용
				for(int i = 0; i < item.size(); i++) {
					
					JSONObject jsonObj = (JSONObject) item.get(i);
					
					ToiletInfoVo toiletInfoVo = new ToiletInfoVo();
					
					toiletInfoVo.setDataCd( (String)jsonObj.get("dataCd") );
					toiletInfoVo.setLaCrdnt( (String)jsonObj.get("laCrdnt") );
					toiletInfoVo.setLoCrdnt( (String)jsonObj.get("loCrdnt") );
					toiletInfoVo.setEmdNm( (String)jsonObj.get("emdNm") );
					toiletInfoVo.setRnAdres( (String)jsonObj.get("rnAdres") );
					toiletInfoVo.setLnmAdres( (String)jsonObj.get("lnmAdres") );
					toiletInfoVo.setPhotoYn( (String)jsonObj.get("photoYn") );
					toiletInfoVo.setEtcCn( (String)jsonObj.get("etcCn") );
					toiletInfoVo.setToiletNm( (String)jsonObj.get("toiletNm") );
					toiletInfoVo.setOpnTimeInfo( (String)jsonObj.get("opnTimeInfo") );
					toiletInfoVo.setMwmnCmnuseToiletYn( (String)jsonObj.get("mwmnCmnuseToiletYn") );
					
					
					if( "N".equals(toiletInfoVo.getPhotoYn()) ) {
						toiletInfoVo.setPhoto(null);
					} else {
						toiletInfoVo.setPhoto((List<String>) jsonObj.get("photo"));
					}
					
					resultList.add(toiletInfoVo);
					
				}
				
				for (ToiletInfoVo vo : resultList) {
		            System.out.println(vo);
		        }
				

		
			} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error : " + e);
		}
		
		return resultList;
	}

}
