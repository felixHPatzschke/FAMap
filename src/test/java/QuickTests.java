import org.junit.Test;

/**
 * Created by Basti on 19.7.16.
 */
public class QuickTests {
    @Test
    public void quickTest(){
        int x=0,y;
        boolean[][] otherChanges = new boolean[][]{
                {true,false,true},
                {false,true,false}
        };
        while(x<otherChanges.length){
            y=0;
            while (y<otherChanges[0].length){
                System.out.println(otherChanges[x][y]);
                y++;
            }
            x++;
        }
    }
}
