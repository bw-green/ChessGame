작업 하실 때 메인에서 하시지 마시고 브랜치 파서하시고 main으로 푸시 금지입니다


특스룰 토글을 게임메니저에서 관리
게임종류 구별 ->
인자를 보드로 보냄
보드 한개에서 게임 4개 관리

토글 3개인자랑 게임 변수 1개, intanceof
주의 특수룰을 할때 return값
test도 똑같이 해줘야함

캐슬링
MoveErrorType.INVALID_MOVE_FOR_THIS_PIECE;
줘야함

앙파상은
3번 이동 수행에서
((Pawn) end.getPiece()).enPassantable=false; 넣어주기

프로모션
promotionGood을 이용한 야비한 수법통해 설계량 대폭 줄여버리기

장점: extenstion -> overide 설명을 한번씩 설명
구현난이도 어렵지않다,
메소드 인자가 전체 설명해야함
