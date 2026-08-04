public class Count{
    public int count(String[] e){
        int count=0;
       /*for(int i=0; i<e.length; i++){
            count++;
        }*/
        for(String nb : e){
            count++;
        }
        return count;
    }
}