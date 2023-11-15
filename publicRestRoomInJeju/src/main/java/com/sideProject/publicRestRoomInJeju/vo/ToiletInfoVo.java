package com.sideProject.publicRestRoomInJeju.vo;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ToiletInfoVo {
	
	private String dataCd;							// 데이터 코드
	private String laCrdnt;							// 위도 좌표
	private String loCrdnt;							// 경도 좌표
	private String emdNm;							// 읍면동 명
	private String rnAdres;							// 도로명 주소
	private String lnmAdres;						// 지번 주소
	private String photoYn;							// 사진 여부
	private String etcCn;							// 기타 내용
	private String toiletNm;						// 화장실 명
	private String opnTimeInfo;						// 개방 시간 정보
	private String mwmnCmnuseToiletYn;				// 남녀 공용 화장실 여부
	private List<String> photo;						// 사진

}
