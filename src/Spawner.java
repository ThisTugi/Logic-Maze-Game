import java.util.Random;

public class Spawner {
    public char element;

    public void spawnMechanic(){
        Random rnd = new Random();
        int randomElement = rnd.nextInt(1,21);
        if(randomElement == 1){
            element = 'A';
        }else if(randomElement == 2){
            element = 'B';
        }else if(randomElement == 3){
            element = 'C';
        }else if(randomElement == 4){
            element = 'D';
        }else if(randomElement == 5){
            element = 'a';
        }else if(randomElement == 6){
            element = 'b';
        }else if(randomElement == 7){
            element = 'c';
        }else if(randomElement == 8){
            element = 'd';
        }else if(randomElement == 9){
            element = '~';
        }else if(randomElement == 10){
            element = '^';
        }else if(randomElement == 11){
            element = 'v';
        }else if(randomElement == 12){
            element = '+';
        }else if(randomElement == 13){
            element = '>';
        }else if(randomElement == 14){
            element = '=';
        }else if(randomElement == 15 || randomElement == 16 || randomElement == 17 || randomElement == 18){
            element = '@';
        }else if(randomElement == 19 || randomElement == 20){
            element = 'X';
        }



    }
}
