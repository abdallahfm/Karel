import stanford.karel.*;
public class Homework extends SuperKarel {
    int numOfMoves=0,numOfBeepers=0;

    public void move(){
        super.move();
        numOfMoves++;
    }
    public void putBeeper(){
        super.putBeeper();
          numOfBeepers++;
    }
    public void turnAndMove(String side,int firstMoves , int secMoves,boolean beeper1,boolean beeper2){
        move(beeper1,firstMoves);
        if(side.equals("left"))turnLeft();
        else turnRight();
        move(beeper2,secMoves);
    }
    public  int getDistance() {// this function returns the length of the rest of the line it can be used to find the width or height
        int i=1;
        for(;!frontIsBlocked();i++)move();
       return i;
    }
    public void move(boolean beeper,int dist){
        // you can move with or without putting a beeper( by toggling the boolean ) for a fixed number of moves
        while(dist-->0){
            if(beeper) putBeeper();
            move();
        }
        if(beeper) putBeeper();
    }
    public void diagonal(int len){
//this function lets karel move diagonally and put beeper on the diagonal
        int temp=len-1;
        while(len-- >0){
            putBeeper();  
            if(len==temp) turnLeft();
            else turnRight();
            turnAndMove("left",1, 1,false,false);
        }
        putBeeper();
    }
    public void cross(int width ,int height){
//this function divides the map into a cross shape either double or single lined
        int dist = width % 2 == 0 ? (width - 1) / 2 : width / 2; //deciding the distance required based on the width
        turnLeft();
        turnAndMove("left",dist ,height-1,false,true);
        if (width%2==0) {
            //even = double lined
            turnRight();
            turnAndMove("right",1,height-1,false,true);

        }
        turnLeft();
        move(false, dist);turnLeft();
        dist = height % 2 == 0 ? (height - 1) / 2 : height / 2;
        turnAndMove("left",dist ,width-1,false,true);
        if(height%2==0) {
            turnRight();
            turnAndMove("right",1 ,width-1,false,true);
        }
    }
    public void column(int width , int height ){

        int fill=(width -3)%4 , gap = (width-3)/4;
        boolean isOnTop= fill != 1 && fill != 3;

        for(int i=1;fill>0;i++){
            if(i==1) turnAround();
            else if(i==2) {
                turnRight();move();turnRight();
            }
            else {
                turnLeft();move();turnLeft();
            }
            move(true,height-1);fill--;
        }

        if(isOnTop)turnLeft(); else turnRight();
        if((width-3)%4!=0)move();
        turnAndMove(isOnTop?"left":"right",gap ,height-1,false,true);
        if(isOnTop)turnRight();else turnLeft();
        move();
        turnAndMove(isOnTop?"right":"left",gap ,height-1,false,true);
        if(isOnTop)turnLeft();else turnRight();
        move();
        turnAndMove(isOnTop?"left":"right",gap ,height-1,false,true);
    }
    public void row(int width,int height){
        int fill=(height -3)%4 , gap = (height-3)/4 ;
        boolean onRight= fill != 1 && fill != 3;
        for(int i=1;fill>0;i++){
            if(i==1)  turnLeft();
            else if(i==2) {
                turnLeft();move();turnLeft();
            }
            else {
                turnRight();move();turnRight();
            }
            move(true,width-1);fill--;
        }

        if(onRight && (height-3)%4==0)turnAround();
        else{
            if(onRight)turnRight();else turnLeft();
            move();
        }
       turnAndMove(onRight?"right":"left",gap ,width-1,false,true);
       if(onRight) turnLeft();  else turnRight();
       move();
       turnAndMove(onRight?"left":"right",gap ,width-1,false,true);
       if(onRight) turnRight(); else turnLeft();
       move();
       turnAndMove(onRight?"right":"left",gap ,width-1,false,true);
    }
    public void nonFourDivider(int width ,int height){
        int fill=(width>=5?(width -2)%3: (width -1)%2 ),gap=(width>=5?(width-2)/3: (width-1)/2); //deciding if we can divide it to 3 or 2
        if(width>height){
            if(fill>0){
                turnAround();
                turnAndMove("right",height-1 ,1,true,false);
            }
            else turnLeft();
            turnAndMove( (fill>0?"right":"left") ,gap ,height-1,false,true );
            if(width<5)return;
            if(fill>0)turnLeft();else turnRight();
            move();
            turnAndMove(fill>0?"left":"right",gap ,height-1,false,true);

        }else{
            fill=height>=5?(height -2)%3:(height -1)%2;
            gap=height>=5?(height -2)/3:(height -1)/2;
            if(fill>0){
                turnLeft();
                turnAndMove("left",width-1 ,1,true,false);
            }
            else turnAround();
            turnAndMove(fill>0?"left":"right",gap ,width-1,false,true);
            if(height<5)return;
            if(fill>0)turnRight(); else turnLeft();
            move();
            turnAndMove(fill>0?"right":"left",gap ,width-1,false,true);
        }
    }
    public int columnMoveCount(int w, int h){
        return 3*(h-1)+((w-1)-((w-3)/4))+((w-3)%4)*(h-1);
    }
    public int crossMoveCount(int width, int height){
        if(width%2==0 && height%2==0)return 3*(width-1)+2*(height-1)+(height)/2;
        if (width%2==0)return 2*(width-1)+5*(height-1)/2;
        if(height%2==0 )  return 3*(width-1)+(height-1)+(height/2);
        return  2*(width-1)+3*(height-1)/2;
    }
    public void run()   {
        setBeepersInBag(10000);
        int width=getDistance();turnLeft(); int height=getDistance(); // get the width and the height
        //now we need to know the category
        if((width==1 && height ==2) || (width==1 &&  height ==1) || (width==2 &&  height ==1) ){
            //not dividable
            System.out.println("can't be divided ! "); return ;
        }
        if(width==2 && height ==2) diagonal(1); //special case 2x2
        else {
            if (width == height) {//equal w and h
                if (width % 2 == 0) {
                    diagonal(width - 1);turnLeft();
                    move(false,width-1);
                    diagonal(width - 1);
                } else cross(width, height);
            }
            else {//not equal
                 if (height >= 7 || width >= 7) {//one of them is larger than 7
                    if (height >= 7 && width >= 7 && columnMoveCount(Math.max(width,height),Math.min(width,height)) <crossMoveCount(width,height)) {
                        //both of them are larger than 7 and the one larger is better than cross
                            if (width > height) column(width, height);
                            else row(width, height);
                    } else if (width >= 7 && columnMoveCount(width, height) < crossMoveCount(width, height))//only width is larger than7 and it's better than cross
                        column(width, height);
                      else if(columnMoveCount(height, width) < crossMoveCount(width, height))//only height is larger than7 and it's better than cross
                          row(width, height);
                      else cross(width, height);// none of them is better than cross
                }
                 else if(width <=2 || height<=2 )nonFourDivider(width,height); // can't be divided by 4
                 else cross(width, height);//cross is the best way to divide
            }
        }
       System.out.println("number of moves to build the chambers: "+ (numOfMoves-((width-1 )+ (height-1))));
       System.out.println("total number of moves : "+numOfMoves +"\ntotal number of beepers : "+numOfBeepers);
       numOfMoves=0;numOfBeepers=0;
    }
}