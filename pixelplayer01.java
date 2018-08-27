import java.awt.Point;

/* 1.둘 수 있는 곳이 없을 때 : 무승부
 * 2.둘 수 있는 곳이 한곳일 때 : 무조건 그 곳에 둬야 한다
 * 3.둘 수 있는 곳이 여러곳일 때 :
 *   1) 근처에 내 돌 세개가 없는 곳(= 뒀을 때 내가 4목이 되는 곳이 아닌 곳) // 배열에 3으로 저장
 *   2) 상대방이 세개가 있는 곳 빼고(= 상대방이 4목이 될 수 있는 곳이 아닌 곳)//배열에 4로 저장
 *   		-가장 외곽부터 둘 수 있도록
 *   3) 근처에 내 돌 두개가 없는 곳 //배열에 5로 저장
 *   		-가장 외곽부터 둘 수 있도록
 *   4) 남아있는 모든 곳의 근처에 같은 돌이 세개 일때는 어쩔 수 없지만 아무 곳에 넣는다(=지는 것)
 */

public class PixelPlayer01 extends Player {
	PixelPlayer01(int[][] map) {
		super(map);
	}

	public Point nextPosition(Point lastPosition) {  
		int x = (int)lastPosition.getX(), y = (int)lastPosition.getY(); //상대방이 마지막으로 둔위치 x,y
		int myNum = map[(int)currentPosition.getX()][(int)currentPosition.getY()];//currentPosition은 최근 자기가 둔거 위치 저장
		int yourNum = map[(int)lastPosition.getX()][(int)lastPosition.getY()];//상대방의 돌 색깔을 나타내는 숫자

		Point nextPosition;//반환할 nextPosition변수 선언
		int[] xPosition=new int[PixelTester.SIZE_OF_BOARD]; //세로로 어디가 비었는지, 좋은 자리인지의 정보를 넣은 배열
		int[] yPosition=new int[PixelTester.SIZE_OF_BOARD]; //가로로 어디가 비었는지, 좋은 자리인지의 정보를 넣은 배열
		
		for(int i = 0; i < PixelTester.SIZE_OF_BOARD ; i++){// 배열 -1(= 둘 수 없는 곳)로 초기화  
			xPosition[i] = -1;
			yPosition[i] = -1;
		}
		
		int count = 0; //빈 곳이 몇 개 남았는지 세는 변수
		//둘 수 있는 곳(= 0)이 얼마나 있는지 확인
		for(int i = 0; i < PixelTester.SIZE_OF_BOARD ; i++){//세로로 어디가 비었는지 확인
			if(map[i][y] == 0){
				count++; 
				xPosition[i] = 0; //그 위치들 기억
			}
		}
		for(int j = 0; j < PixelTester.SIZE_OF_BOARD ; j++){//가로로 어디가 비었는지 확인
			if(map[x][j] == 0){
				count++;
				yPosition[j] = 0; // 그 위치들 기억
			}
		}
		
		//판단해서 돌 놓기
		if(count == 0){ //둘 곳 없을 때는 그냥 똑같은 거 반환해서 무승부 알려줌
			nextPosition = new Point(x, y);
			return nextPosition;
		}
		else if(count == 1){ //둘 곳 한 곳 남았을 때는 무조건 그곳에 둘 수 있도록
			for(int i = 0; i < PixelTester.SIZE_OF_BOARD ; i ++){
				if(xPosition[i] == 0){ //세로라인부터 남은 곳 확인
					nextPosition = new Point(i, y);
					return nextPosition;
				}
				if(yPosition[i] == 0){ //가로라인 남은 곳 확인해서 입력
					nextPosition = new Point(x, i);
					return nextPosition;
				}
			}
		}
		else{ //둘 수 있는 곳이 여러 곳 남았을 때 
			for(int i = 0; i < PixelTester.SIZE_OF_BOARD ; i ++){
				boolean isBadPlace = false; //안좋은 자리 있는지 확인하는 변수(근처에 내 돌이 세개 있을 때)

				//BadPlace(근처에 내 것이 세개 있는 위치)확인
				//세로선먼저 확인하자!
				if(xPosition[i] == 0){
					//세로줄의 상향 대각선 
					//첫번째 위치에 놓일 때 확인
					if (i >= 0 && i <= 4 && y >= 3 && y <= 7 && map[i + 1][y - 1] == myNum 
							&& map[i + 2][y - 2] == myNum && map[i + 3][y - 3] == myNum) {
						isBadPlace = true;
					}
					// 두번째 위치에 놓일 때 확인
					else if (i >= 1 && i <= 5 && y >= 2 && y <= 6 && map[i - 1][y + 1] == myNum
							&& map[i + 1][y - 1] == myNum && map[i + 2][y - 2] == myNum) {
						isBadPlace = true;
					}
					// 세번째 위치에 놓일 때 확인
					else if (i >= 2 && i <= 6 && y >= 1 && y <= 5 && map[i - 2][y + 2] == myNum
							&& map[i - 1][y + 1] == myNum && map[i + 1][y - 1] == myNum) {
						isBadPlace = true;
					}
					// 네번째 위치에 놓일 때 확인
					else if (i >= 3 && i <= 7 && y >= 0 && y <= 4 && map[i - 3][y + 3] == myNum
							&& map[i - 2][y + 2] == myNum && map[i - 1][y + 1] == myNum) {
						isBadPlace = true;
					}
					// 세로줄의 햐향대각선 확인
					// 첫번째 위치에 놓일 때 확인
					if (i >= 3 && i <= 7 && y >= 3 && y <= 7 && map[i - 1][y - 1] == myNum 
							&& map[i - 2][y - 2] == myNum && map[i - 3][y - 3] == myNum) {
						isBadPlace = true;
					}
					// 두번째 위치에 놓일 때 확인
					else if (i >= 2 && i <= 6 && y >= 2 && y <= 6 && map[i - 2][y - 2] == myNum
							&& map[i - 1][y - 1] == myNum && map[i + 1][y + 1] == myNum) {
						isBadPlace = true;
					}
					// 세번째 위치에 놓일 때 확인
					else if (i >= 1 && i <= 5 && y >= 1 && y <= 5 && map[i - 1][y - 1] == myNum
							&& map[i + 1][y + 1] == myNum && map[i + 2][y + 2] == myNum) {
						isBadPlace = true;
					}
					// 네번째 위치에 놓일 때 확인
					else if (i >= 0 && i <= 4 && y >= 0 && y <= 4 && map[i + 1][y + 1] == myNum
							&& map[i + 2][y + 2] == myNum && map[i + 3][y + 3] == myNum) {
						isBadPlace = true;
					}
					//세로줄의 세로 확인
					if(i >= 0 && i <= 4 && map[i+1][y]==myNum && map[i+2][y]==myNum && map[i+3][y]==myNum){//첫번째
						isBadPlace= true;
					}
					else if(i >= 1 && i <= 5 && map[i-1][y]==myNum && map[i+1][y]==myNum && map[i+2][y]==myNum){//두번째
						isBadPlace= true;
					}
					else if(i >= 2 && i <= 6 && map[i-2][y]==myNum && map[i-1][y]==myNum && map[i+1][y]==myNum){//세번째
						isBadPlace= true;
					}
					else if(i >= 3 && i <= 7 && map[i-3][y]==myNum && map[i-2][y]==myNum && map[i-1][y]==myNum){//네번째
						isBadPlace= true;
					}
					//세로줄의 가로 확인
					if(y >= 0 && y <= 4 && map[i][y+1]==myNum && map[i][y+2]==myNum && map[i][y+3]==myNum){//첫번째
						isBadPlace= true;
					}
					else if(y >= 1 && y <= 5 && map[i][y-1]==myNum && map[i][y+1]==myNum && map[i][y+2]==myNum){//두번째
						isBadPlace= true;
					}
					else if(y >= 2 && y <= 6 && map[i][y-2]==myNum && map[i][y-1]==myNum && map[i][y+1]==myNum){//세번째
						isBadPlace= true;
					}
					else if(y >= 3 && y <= 7 && map[i][y-3]==myNum && map[i][y-2]==myNum && map[i][y-1]==myNum){//네번째
						isBadPlace= true;
					}
					//세로줄 확인 끝나면
					if(isBadPlace == false){
						xPosition[i] = 3;//나한테 불리한 곳 아니면 3을 넣는다.
					}
				}
				
				//이제 가로선 확인시작!
				if(yPosition[i] == 0){
					//가로줄의 상향 대각선
					//첫번째 위치에 놓일 때 확인
					if (x >= 0 && x <= 4 && i >= 3 && i <= 7 && map[x + 1][i - 1] == myNum 
							&& map[x + 2][i - 2] == myNum && map[x + 3][i - 3] == myNum) {
						isBadPlace = true;
					}
					// 두번째 위치에 놓일 때 확인
					else if (x >= 1 && x <= 5 && i >= 2 && i <= 6 && map[x - 1][i + 1] == myNum
							&& map[x + 1][i - 1] == myNum && map[x + 2][i - 2] == myNum) {
						isBadPlace = true;
					} 
					//세번째 위치에 놓일 때 확인
					else if (x >= 2 && x <= 6 && i >= 1 && i <= 5 && map[x - 2][i + 2] == myNum
							&& map[x - 1][i + 1] == myNum && map[x + 1][i - 1] == myNum) {
						isBadPlace = true;
					} 
					//네번째 위치에 놓일 때 확인
					else if (x >= 3 && x <= 7 && i >= 0 && i <= 4 && map[x - 3][i + 3] == myNum
							&& map[x - 2][i + 2] == myNum && map[x - 1][i + 1] == myNum) {
						isBadPlace = true;
					}
					//가로줄의  햐향대각선 확인
					//네번째 위치에 놓일 때 확인
					if (x >= 0 && x <= 4 && i >= 0 && i <= 4 && map[x + 1][i + 1] == myNum && map[x + 2][i + 2] == myNum
							&& map[x + 3][i + 3] == myNum) {			
						isBadPlace = true;
					} 
					//세번째 위치에 놓일 때 확인
					else if (x >= 1 && x <= 5 && i >= 1 && i <= 5 && map[x - 1][i - 1] == myNum
							&& map[x + 1][i + 1] == myNum && map[x + 2][i + 2] == myNum) {
						isBadPlace = true;
					} 
					//두번째 위치에 놓일 때 확인
					else if (x >= 2 && x <= 6 && i >= 2 && i <= 6 && map[x - 2][i - 2] == myNum
							&& map[x - 1][i - 1] == myNum && map[x + 1][i + 1] == myNum) {
						isBadPlace = true;
					} 
					//첫번째 위치에 놓일 때 확인
					else if (x >= 3 && x <= 7 && i >= 3 && i <= 7 && map[x - 3][i - 3] == myNum
							&& map[x - 2][i - 2] == myNum && map[x - 1][i - 1] == myNum) {
						isBadPlace = true;
					}
					//가로줄의 세로 확인하고
					//첫번째 위치에 놓일 때 확인
					if(x >= 0 && x <= 4 && map[x+1][i]==myNum && map[x+2][i]==myNum && map[x+3][i]==myNum){
						isBadPlace= true;
					}
					//두번째 위치에 놓일 때 확인
					else if(x >= 1 && x <= 5 && map[x-1][i]==myNum && map[x+1][i]==myNum && map[x+2][i]==myNum){
						isBadPlace= true;
					}
					//세번째 위치에 놓일 때 확인
					else if(x >= 2 && x <= 6 && map[x-2][i]==myNum && map[x-1][i]==myNum && map[x+1][i]==myNum){
						isBadPlace= true;
					}
					//네번째 위치에 놓일 때 확인
					else if(x >= 3 && x <= 7 && map[x-3][i]==myNum && map[x-2][i]==myNum && map[x-1][i]==myNum){
						isBadPlace= true;
					}
					//가로줄의 가로 확인하고
					//첫번째 위치에 놓일 때 확인
					if(i >= 0 && i <= 4 && map[x][i+1]==myNum && map[x][i+2]==myNum && map[x][i+3]==myNum){
						isBadPlace= true;
					}
					//두번째 위치에 놓일 때 확인
					else if(i >= 1 && i <= 5 && map[x][i-1]==myNum && map[x][i+1]==myNum && map[x][i+2]==myNum){
						isBadPlace= true;
					}
					//세번째 위치에 놓일 때 확인
					else if(i >= 2 && i <= 6 && map[x][i-2]==myNum && map[x][i-1]==myNum && map[x][i+1]==myNum){
						isBadPlace= true;
					}
					//네번째 위치에 놓일 때 확인
					else if(i >= 3 && i <= 7 && map[x][i-3]==myNum && map[x][i-2]==myNum && map[x][i-1]==myNum){//네번째
						isBadPlace= true;
					}
					//가로줄 확인 끝나면
					if(isBadPlace == false){
						yPosition[i] = 3; //나한테 불리한 곳이 아니라는 뜻의 3 넣음
					}
				}
			}//BadPlace의 위치를 확인하기 위한 for문 끝

			//BadPlace가 얼마나 있는지 확인하는 구간
			int find_3 = 0; //BadPlace가 아닌 곳(3)을 세기 위한 count 변수
			for(int i = 0; i < PixelTester.SIZE_OF_BOARD; i++ ){
				if(xPosition[i] == 3){
					find_3++;
				}
				if(yPosition[i] == 3){
					find_3++;
				}
			}
			// BadPlace가 얼마나 있는지 확인하는 구간 끝
			
			if(find_3 == 0){//모두 BadPlace일때 (3이 하나도 없을 때) 빈 곳에 차례로 넣음 
				for(int i = 0; i < PixelTester.SIZE_OF_BOARD ; i ++){
					if(xPosition[i] == 0){ //세로부터 남은 곳 확인
						nextPosition = new Point(i, y);
						return nextPosition;
					}
					if(yPosition[i] == 0){ //가로 남은 곳 확인해서 입력
						nextPosition = new Point(x, i);
						return nextPosition;
					}
				}
			}
			else if(find_3 != 0){//BadPlace가 아닌 구간 있으면
				for(int i = 0; i< PixelTester.SIZE_OF_BOARD; i++){
					boolean isGoodPlace = false; // 상대방을 공격하기 위한 좋은 자리 = 상대방이 놓았을 때 사목이 되는 자리

					//GoodPlace(근처에 상대방 것이 세개 있는 위치)확인
					//세로선먼저 확인
					if(xPosition[i] == 3){
						//상향 대각선 
						//첫번째 위치에 놓일 때 확인
						if (i >= 0 && i <= 4 && y >= 3 && y <= 7 && map[i + 1][y - 1] == yourNum
								&& map[i + 2][y - 2] == yourNum && map[i + 3][y - 3] == yourNum) {
							isGoodPlace = true;
						}
						// 두번째 위치에 놓일 때 확인
						else if (i >= 1 && i <= 5 && y >= 2 && y <= 6 && map[i - 1][y + 1] == yourNum
								&& map[i + 1][y - 1] == yourNum && map[i + 2][y - 2] == yourNum) {
							isGoodPlace = true;
						}
						// 세번째 위치에 놓일 때 확인
						else if (i >= 2 && i <= 6 && y >= 1 && y <= 5 && map[i - 2][y + 2] == yourNum
								&& map[i - 1][y + 1] == yourNum && map[i + 1][y - 1] == yourNum) {
							isGoodPlace = true;
						}
						// 네번째 위치에 놓일 때 확인
						else if (i >= 3 && i <= 7 && y >= 0 && y <= 4 && map[i - 3][y + 3] == yourNum
								&& map[i - 2][y + 2] == yourNum && map[i - 1][y + 1] == yourNum) {
							isGoodPlace = true;
						}
						//세로줄의  햐향대각선 확인
						//첫번째 위치에 놓일 때 확인
						if (i >= 3 && i <= 7 && y >= 3 && y <= 7 && map[i - 3][y - 3] == yourNum
								&& map[i - 2][y - 2] == yourNum && map[i - 1][y - 1] == yourNum) {
							isGoodPlace = true;
						}
						// 두번째 위치에 놓일 때 확인
						else if (i >= 2 && i <= 6 && y >= 2 && y <= 6 && map[i - 2][y - 2] == yourNum
								&& map[i - 1][y - 1] == yourNum && map[i + 1][y + 1] == yourNum) {
							isGoodPlace = true;
						}
						// 세번째 위치에 놓일 때 확인
						else if (i >= 1 && i <= 5 && y >= 1 && y <= 5 && map[i - 1][y - 1] == yourNum
								&& map[i + 1][y + 1] == yourNum && map[i + 2][y + 2] == yourNum) {
							isGoodPlace = true;
						}
						// 네번째 위치에 놓일 때 확인
						else if (i >= 0 && i <= 4 && y >= 0 && y <= 4 && map[i + 1][y + 1] == yourNum
								&& map[i + 2][y + 2] == yourNum && map[i + 3][y + 3] == yourNum) {
							isGoodPlace = true;
						}
						//세로줄의 세로
						//첫번째 위치에 놓일 때 확인
						if(i >= 0 && i <= 4 && map[i+1][y]==yourNum && map[i+2][y]==yourNum && map[i+3][y]==yourNum){
							isGoodPlace= true;
						}
						//두번째 위치에 놓일 때 확인
						else if(i >= 1 && i <= 5 && map[i-1][y]==yourNum && map[i+1][y]==yourNum && map[i+2][y]==yourNum){
							isGoodPlace= true;
						}
						//세번째 위치에 놓일 때 확인
						else if(i >= 2 && i <= 6 && map[i-2][y]==yourNum && map[i-1][y]==yourNum && map[i+1][y]==yourNum){
							isGoodPlace= true;
						}
						//네번째 위치에 놓일 때 확인
						else if(i >= 3 && i <= 7 && map[i-3][y]==yourNum && map[i-2][y]==yourNum && map[i-1][y]==yourNum){
							isGoodPlace= true;
						}
						//세로줄의 가로 확인
						//첫번째 위치에 놓일 때 확인
						if(y >= 0 && y <= 4 && map[i][y+1]==yourNum && map[i][y+2]==yourNum && map[i][y+3]==yourNum){
							isGoodPlace= true;
						}
						//두번째 위치에 놓일 때 확인
						else if(y >= 1 && y <= 5 && map[i][y-1]==yourNum && map[i][y+1]==yourNum && map[i][y+2]==yourNum){
							isGoodPlace= true;
						}
						//세번째 위치에 놓일 때 확인
						else if(y >= 2 && y <= 6 && map[i][y-2]==yourNum && map[i][y-1]==yourNum && map[i][y+1]==yourNum){
							isGoodPlace= true;
						}
						//네번째 위치에 놓일 때 확인
						else if(y >= 3 && y <= 7 && map[i][y-3]==yourNum && map[i][y-2]==yourNum && map[i][y-1]==yourNum){
							isGoodPlace= true;
						}
						//세로줄 확인 끝나면
						if(isGoodPlace == false){ //좋은 자리 아니면
							xPosition[i] = 4; //GoodPlace아닌 자리 최우선으로 놓아야 할 자리
						}
					}
					//GoodPlace(근처에 상대방 것이 세개 있는 위치)세로줄 확인 끝

					//이제 가로선 확인시작!
					if(yPosition[i] == 3){
						//상향 대각선
						//첫번째 위치에 놓일 때 확인
						if (x >= 0 && x <= 4 && i >= 3 && i <= 7 && map[x + 1][i - 1] == yourNum
								&& map[x + 2][i - 2] == yourNum && map[x + 3][i - 3] == yourNum) {
							isGoodPlace = true;
						}
						// 두번째 위치에 놓일 때 확인
						else if (x >= 1 && x <= 5 && i >= 2 && i <= 6 && map[x - 1][i + 1] == yourNum
								&& map[x + 1][i - 1] == yourNum && map[x + 2][i - 2] == yourNum) {
							isGoodPlace = true;
						}
						// 세번째 위치에 놓일 때 확인
						else if (x >= 2 && x <= 6 && i >= 1 && i <= 5 && map[x - 2][i + 2] == yourNum
								&& map[x - 1][i + 1] == yourNum && map[x + 1][i - 1] == yourNum) {
							isGoodPlace = true;
						}
						// 네번째 위치에 놓일 때 확인
						else if (x >= 3 && x <= 7 && i >= 0 && i <= 4 && map[x - 3][i + 3] == yourNum
								&& map[x - 2][i + 2] == yourNum && map[x - 1][i + 1] == yourNum) {
							isGoodPlace = true;
						}
						// 가로 줄의 햐향대각선 확인
						// 네번째 위치에 놓일 때 확인
						if (x >= 0 && x <= 4 && i >= 0 && i <= 4 && map[x + 1][i + 1] == yourNum
								&& map[x + 2][i + 2] == yourNum && map[x + 3][i + 3] == yourNum) {
							isGoodPlace = true;
						}
						// 세번째 위치에 놓일 때 확인
						else if (x >= 1 && x <= 5 && i >= 1 && i <= 5 && map[x - 1][i - 1] == yourNum
								&& map[x + 1][i + 1] == yourNum && map[x + 2][i + 2] == yourNum) {
							isGoodPlace = true;
						}
						// 두번째 위치에 놓일 때 확인
						else if (x >= 2 && x <= 6 && i >= 2 && i <= 6 && map[x - 2][i - 2] == yourNum
								&& map[x - 1][i - 1] == yourNum && map[x + 1][i + 1] == yourNum) {
							isGoodPlace = true;
						}
						// 첫번째 위치에 놓일 때 확인
						else if (x >= 3 && x <= 7 && i >= 3 && i <= 7 && map[x - 3][i - 3] == yourNum
								&& map[x - 2][i - 2] == yourNum && map[x - 1][i - 1] == yourNum) {
							isGoodPlace = true;
						}
						//가로줄의 세로 확인
						//첫번째 위치에 놓일 때 확인
						if(x >= 0 && x <= 4 && map[x+1][i]==yourNum && map[x+2][i]==yourNum && map[x+3][i]==yourNum){
							isGoodPlace= true;
						}
						//두번째 위치에 놓일 때 확인
						else if(x >= 1 && x <= 5 && map[x-1][i]==yourNum && map[x+1][i]==yourNum && map[x+2][i]==yourNum){
							isGoodPlace= true;
						}
						//세번째 위치에 놓일 때 확인
						else if(x >= 2 && x <= 6 && map[x-2][i]==yourNum && map[x-1][i]==yourNum && map[x+1][i]==yourNum){
							isGoodPlace= true;
						}
						//네번째 위치에 놓일 때 확인
						else if(x >= 3 && x <= 7 && map[x-3][i]==yourNum && map[x-2][i]==yourNum && map[x-1][i]==yourNum){
							isGoodPlace= true;
						}
						//가로줄의 가로 확인
						//첫번째 위치에 놓일 때 확인
						if(i >= 0 && i <= 4 && map[x][i+1]==yourNum && map[x][i+2]==yourNum && map[x][i+3]==yourNum){
							isGoodPlace= true;
						}
						//두번째 위치에 놓일 때 확인
						else if(i >= 1 && i <= 5 && map[x][i-1]==yourNum && map[x][i+1]==yourNum && map[x][i+2]==yourNum){
							isGoodPlace= true;
						}
						//세번째 위치에 놓일 때 확인
						else if(i >= 2 && i <= 6 && map[x][i-2]==yourNum && map[x][i-1]==yourNum && map[x][i+1]==yourNum){
							isGoodPlace= true;
						}
						//네번째 위치에 놓일 때 확인
						else if(i >= 3 && i <= 7 && map[x][i-3]==yourNum && map[x][i-2]==yourNum && map[x][i-1]==yourNum){//네번째
							isGoodPlace= true;
						}
						//가로줄 확인 끝나면
						if(isGoodPlace == false){ //좋은 자리 아니면
							yPosition[i] = 4; //GoodPlace아닌 자리 
						}
					}
					//GoodPlace(근처에 상대방 것이 세개 있는 위치) 가로줄 확인 끝
				}//GoodPlace검색하는 for문 끝
				
				
			
				//여기 부터는 GoodPlace가 얼마나 있는지 확인하는 구간
				int find_4 = 0; //GoodPlace가 아닌 곳(=4)이 몇개인지 확이하는 count변수
				for(int i = 0; i < PixelTester.SIZE_OF_BOARD; i++ ){
					if(xPosition[i] == 4){
						find_4++;
					}
					if(yPosition[i] == 4){
						find_4++;
					}
				}
				//GoodPlace가 얼마나 있는지 확인하는 구간 끝
				if(find_4 == 0){//모두 GoodPlace일 때 (=4가 하나도 없을 때)차례로 넣음 
					for(int i = 0; i < PixelTester.SIZE_OF_BOARD ; i ++){
						if(xPosition[i] == 3){ //세로부터 남은 곳 확인
							nextPosition = new Point(i, y);
							return nextPosition;
						}
						if(yPosition[i] == 3){ //가로 남은 곳 확인해서 입력
							nextPosition = new Point(x, i);
							return nextPosition;
						}
					}
				}
				else if (find_4 != 0) { // GoodPlace가 아닌 구간 있으면
					for (int i = 0; i < PixelTester.SIZE_OF_BOARD; i++) {
						boolean isNotAtrractivePlace = false; // 근처에 내것이 두개 있는 위치
						// NotAtrractivePlace(근처에 내 것이 두개 있는 위치)확인
						// 세로선먼저 확인
						if (xPosition[i] == 4) {
							// 상향 대각선
							// 첫번째 위치에 놓일 때 확인
							if (i >= 0 && i <= 5 && y >= 2 && y <= 7 && map[i + 1][y - 1] == myNum
									&& map[i + 2][y - 2] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 두번째 위치에 놓일 때 확인
							else if (i >= 1 && i <= 6 && y >= 1 && y <= 6 && map[i - 1][y + 1] == myNum
									&& map[i + 1][y - 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세번째 위치에 놓일 때 확인
							else if (i >= 2 && i <= 7 && y >= 0 && y <= 5 && map[i - 2][y + 2] == myNum
									&& map[i - 1][y + 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세로줄의 햐향대각선 확인
							// 첫번째 위치에 놓일 때 확인
							if (i >= 2 && i <= 7 && y >= 2 && y <= 7 && map[i - 2][y - 2] == myNum
									&& map[i - 1][y - 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 두번째 위치에 놓일 때 확인
							else if (i >= 1 && i <= 6 && y >= 1 && y <= 6 && map[i - 1][y - 1] == myNum
									&& map[i + 1][y + 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세번째 위치에 놓일 때 확인
							else if (i >= 0 && i <= 5 && y >= 0 && y <= 5 && map[i + 1][y + 1] == myNum
									&& map[i + 2][y + 2] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세로줄의 세로
							// 첫번째 위치에 놓일 때 확인
							if (i >= 0 && i <= 5 && map[i + 1][y] == myNum && map[i + 2][y] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 두번째 위치에 놓일 때 확인
							else if (i >= 1 && i <= 6 && map[i - 1][y] == myNum && map[i + 1][y] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세번째 위치에 놓일 때 확인
							else if (i >= 2 && i <= 7 && map[i - 2][y] == myNum && map[i - 1][y] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세로줄의 가로 확인
							// 세번째 위치에 놓일 때 확인
							if (y >= 0 && y <= 5 && map[i][y + 1] == myNum && map[i][y + 2] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 두번째 위치에 놓일 때 확인
							else if (y >= 1 && y <= 6 && map[i][y - 1] == myNum && map[i][y + 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 첫번째 위치에 놓일 때 확인
							else if (y >= 2 && y <= 7 && map[i][y - 2] == myNum && map[i][y - 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세로줄 확인 끝나면
							if (isNotAtrractivePlace == false) { // 내거 두개 있는 자리 아니면
								xPosition[i] = 5; // NotAtrractivePlace아닌 자리// 최우선으로 놓아야 할 자리
							}
						} // NotAtrractivePlace(근처에 상대방 것이 세개 있는 위치)세로줄 확인 끝

						// 이제 가로선 확인시작!
						if (yPosition[i] == 4) {
							// 상향 대각선
							// 첫번째 위치에 놓일 때 확인
							if (x >= 0 && x <= 5 && i >= 2 && i <= 7 && map[x + 1][i - 1] == myNum
									&& map[x + 2][i - 2] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 두번째 위치에 놓일 때 확인
							else if (x >= 1 && x <= 6 && i >= 1 && i <= 6 && map[x - 1][i + 1] == myNum
									&& map[x + 1][i - 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세번째 위치에 놓일 때 확인
							else if (x >= 2 && x <= 7 && i >= 0 && i <= 5 && map[x - 2][i + 2] == myNum
									&& map[x - 1][i + 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 가로줄의 햐향대각선 확인
							// 세번째 위치에 놓일 때 확인
							if (x >= 0 && x <= 5 && i >= 0 && i <= 5 && map[x + 1][i + 1] == myNum
									&& map[x + 2][i + 2] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 두번째 위치에 놓일 때 확인
							else if (x >= 1 && x <= 6 && i >= 1 && i <= 6 && map[x - 1][i - 1] == myNum
									&& map[x + 1][i + 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 첫번째 위치에 놓일 때 확인
							else if (x >= 2 && x <= 7 && i >= 2 && i <= 7 && map[x - 2][i - 2] == myNum
									&& map[x - 1][i - 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 가로줄의 세로 확인
							// 첫번째 위치에 놓일 때 확인
							if (x >= 0 && x <= 5 && map[x + 1][i] == myNum && map[x + 2][i] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 두번째 위치에 놓일 때 확인
							else if (x >= 1 && x <= 6 && map[x - 1][i] == myNum && map[x + 1][i] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 세번째 위치에 놓일 때 확인
							else if (x >= 2 && x <= 7 && map[x - 2][i] == myNum && map[x - 1][i] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 가로줄의 가로 확인
							// 세번째 위치에 놓일 때 확인
							if (i >= 0 && i <= 5 && map[x][i + 1] == myNum && map[x][i + 2] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 두번째 위치에 놓일 때 확인
							else if (i >= 1 && i <= 6 && map[x][i - 1] == myNum && map[x][i + 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 첫번째 위치에 놓일 때 확인
							else if (i >= 2 && i <= 7 && map[x][i - 2] == myNum && map[x][i - 1] == myNum) {
								isNotAtrractivePlace = true;
							}
							// 가로줄 확인 끝나면
							if (isNotAtrractivePlace == false) { // 내거 두개 있는 자리 아니면
								yPosition[i] = 5; // NotAtrractivePlace아닌 자리// 최우선으로 놓아야 할 자리
							}
						} // NotAtrractivePlace(근처에 내 것이 두개 있는 위치) 가로줄 확인 끝
					} // NotAtrractivePlace검색하는 for문 끝
			
					//NotAtrractivePlace가 아닌 곳(5) 있으면 거기 넣기
					int key = 0; // 단순히 배열의 인덱스를 나타내는 변수명
					for(int k = 0; k < PixelTester.SIZE_OF_BOARD; k++ ){
						if(xPosition[key] == 5){
							nextPosition = new Point(key, y);
							return nextPosition;
						}
						if(yPosition[key] == 5){
							nextPosition = new Point(x, key);
							return nextPosition;
						}
						key = key + (7 - k)* (int)Math.pow(-1,k );//빈 곳이 있다면 양쪽 끝부터 돌을 배치
					}

					// GoodPlace 없으면 나머지에서 아무거나 넣음
					int key2 = 0;// 단순히 배열의 인덱스를 나타내는 변수명
					for(int k = 0; k < PixelTester.SIZE_OF_BOARD; k++ ){
						if(xPosition[key2] == 4){
							nextPosition = new Point(key2, y);
							return nextPosition;
						}
						if(yPosition[key2] == 4){
							nextPosition = new Point(x, key2);
							return nextPosition;
						}
						key2 = key2 + (7 - k)* (int)Math.pow(-1,k );//빈 곳이 있다면 양쪽 끝부터 돌을 배치 
					}
				}//GoodPlace 아닌 구간 있을  확인한 거 끝
			}//BadPlace 아닌 구간 있을  확인한 거 끝	
		}//둘 수 있는 곳 여러곳남았을 의 else문 끝
		nextPosition = new Point(x, y);
		return nextPosition;
		
	}//lastPoint 끝
}
