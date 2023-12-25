
package cs1.MineSweeperApp;

import  cs1.app.*;

public class MineSweeperApp
{
    void printArray(int[] array)
    {
        for (int i = 0; i < array.length; i = i + 1)
        {
            System.out.print(array[i] + " ");
        }
        System.out.println();
    }

    void printTable(int[][] table)
    {
        for (int r = 0; r < table.length; r = r + 1)      
        {
            printArray(table[r]);                       
        }
    }
    
    void plantMines( int[][] field )
    {
        for( int row = 0; row < field.length; row++ )
        {
            for( int col = 0; col < field[0].length; col++ )
            {
                int randomNumber = canvas.getRandomInt( 1, 10 );
                 
                if( randomNumber <= 2 )
                {
                    field[ row ][ col ] = 9;
                }
            }
        }
    }
    
    void setFlag(int[][] field, int row, int col) 
    {
        int currentValue = field[row][col];

        if (currentValue >= 0 && currentValue <= 9) 
        {
            field[row][col] = currentValue + 20;
        }
        else if (currentValue >= 20 && currentValue <= 29) 
        {
            field[row][col] = currentValue - 20;
        }
    } 
    
    boolean isCleared( int[][] field )
    {
        for( int row = 0; row < field.length; row++ )
        {
            for( int col = 0; col < field[ 0 ].length; col++ )
            {
                if( field[ row ][ col ] <= 9 )
                {
                    return false;
                }
                else if( field[ row ][ col ] >= 19 && field[ row ][ col ] <= 28 )
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    void openMines( int[][] field )
    {
        for( int row = 0; row < field.length; row++ )
        {
            for( int col = 0; col < field[ 0 ].length; col++ )
            {
                if( field[ row ][ col ] == 9 )
                {
                    field[ row ][ col ] = 19;
                }
            }
        }
    } 
    
    boolean isValid( int[][] field, int row, int col )
    {
        int numberOfRows = field.length;
        int numberOfCols = field[ 0 ].length;
        
        if( row >= numberOfRows || col >= numberOfCols )
        {
            return false;
        }
        else if( row < 0 || col < 0 )
        {
            return false;
        }
        
        return true;
    }
    
    int countMines( int[][] field, int row, int col )
    {
        int numberOfMines = 0;
        
        for( int i = row - 1; i <= row + 1; i ++ )
        {
            for( int j = col - 1; j <= col + 1; j++ )
            {
                if( isValid( field, i, j ) == true && field[ i ][ j ] == 9 )
                {
                    numberOfMines++;
                }
            }
        }
        return numberOfMines;
    }
    
    void updateField( int[][] field )
    {
        for ( int row = 0; row < field.length; row++ ) 
        {
            for ( int col = 0; col < field[ 0 ].length; col++ ) 
            {
                if ( field[ row ][ col ] != 9 ) 
                {
                    int numberOfMines = countMines( field, row, col );
                    field[ row ][ col ] = numberOfMines;
                }
            }
        }
    }
  
    int[][] generateField( int rows, int cols )
    {
        int[][] field = new int[ rows ][ cols ];
        
        plantMines( field );
        updateField( field );
        
        return field;
    }
    
    void openCell( int[][] field, int row, int col )
    {
        if( field[ row ][ col ] == 0 )
        {
            for( int i = row - 1; i <= row + 1; i++ )
            {
                for( int j = col - 1; j <= col + 1; j++ )
                {
                    if( isValid( field, i, j ) == true && (field[ i ][ j ] >= 0 && field[ i ][ j ] <= 8 ) )
                    {
                        field[ i ][ j ] = field[ i ][ j ] + 10;
                    }
                }
            }
        }
        else if( field[ row ][ col ] < 9 )
        {
            field[ row ][ col ] = field[ row ][ col ] + 10;
        }
    }

    void drawCell( double x, double y, int value )
    {
        String imageName = "m" + value + ".png";
        if( value >= 0 && value <= 9 )
        {
            imageName = "hidden.png";
        }
        canvas.drawImage( x, y, imageName );
    }
    
    void drawField( int[][] field, double startX, double startY )
    {
        double currentX = startX;
        double currentY = startY;
        
        for( int i = 0; i < field.length; i++ )
        {
            for( int j = 0; j < field[ 0 ].length; j++ )
            {
                drawCell( currentX, currentY, field[ i ][ j ] );
                currentX = currentX + 36;
            }
            currentX = startX;
            currentY = currentY + 36;
        }
    }

    void playGame( )
    {
        // setup dimensions of the board
        int rows = canvas.getRandomInt( 7, 10 );   // later pick a random number in [7..10]
        int cols = canvas.getRandomInt( 6, 8 );   // later pick a random number in [6..8]

        // calculate the center (x,y) coordinates of the upper-left square, so that the 
        // board is centered on the screen; you may use 36 as fixed number for image size
        
        int imageSize = 36; // size of each image on the board

        // calculate the center coordinates of the upper-left square
        
        double startX = ( canvas.getWidth() - ( cols * imageSize ) ) / 2 ;
        double startY = ( canvas.getHeight() - ( rows * imageSize ) ) / 2;
        
        // any initial setup (for example, a boolean to keep track if mine tapped)
        boolean isGameOver = false;
        
        String gameState = "smiley";
        
        int[][] field = generateField( rows, cols );
        
        // as long as game is not over:
        while( isGameOver == false )
        {
            // show board, wait for touch, ...
            
            drawField( field, startX, startY );
            
            canvas.drawImage( canvas.getWidth() / 2, 50, "wink.png" );
            canvas.sleep( 0.25 );
            canvas.drawImage(canvas.getWidth() / 2, 50, "smiley.png" );
            
            Touch touch = canvas.waitForTouch();

            // given formulas that use touch Y, X to compute cell R, C:
            int cellRow = ( int ) ( ( touch.getY() - ( startY - imageSize/2 ) ) / imageSize);
            int cellCol = ( int ) ( ( touch.getX() - ( startX - imageSize/2 ) ) / imageSize);
            
            int numTaps = touch.getTaps();

            // update board on single/double tap
            if( numTaps == 1 )
            {
                if( field[ cellRow ][ cellCol ] >= 0 && field[ cellRow ][ cellCol ] <= 8 )
                {
                    openCell( field, cellRow, cellCol );
                }
                else if( field[ cellRow][ cellCol ] == 9 )
                {
                    field[ cellRow ][ cellCol ] = field[ cellRow ][ cellCol ] + 10;
                    isGameOver = true;
                    gameState = "sad";
                }
            }
            else if( numTaps == 2 )
            {
                if( field[ cellRow ][ cellCol ] >= 0 && field[ cellRow ][ cellCol ] <= 9 )
                {
                    field[ cellRow ][ cellCol ] = field[ cellRow ][ cellCol ] + 20;
                }
                else if( field[ cellRow ][ cellCol ] >= 20 && field[ cellRow ][ cellCol ] <= 29 )
                {
                    field[ cellRow ][ cellCol ] = field[ cellRow ][ cellCol ] - 20;
                }
            }
            
            if( isCleared( field ) == true )
            {
                isGameOver = true;
                gameState = "happy";
            }
        }

       // any final actions
        drawField( field, startX, startY );
        String imageName = gameState + ".png";
        
        canvas.drawImage( canvas.getWidth()/ 2, 50, imageName );
    }

    public void run()
    {
        //int[][] field = new int[6][7];
        //plantMines( field );
        //printTable( field );
        
        /*int[][] field = {
            { 9,  9,  2,  1,  0 },
            { 3,  4,  9,  2,  1 },
            { 9, 12,  1,  3,  9 },
            { 1,  1,  0,  2,  9 } };

        setFlag( field, 0, 1 );  
        setFlag( field, 3, 2 );  
        setFlag( field, 1, 1 );  
        setFlag( field, 2, 1 );  

        printTable( field );             
                                 
        */
        /*
        int[][] field = {
            { 9,  29,   2,   1, 20 },
            {23,  24,   9,  12,  1 },
            { 9,  12,  11,  13,  9 },
            { 1,  11,  10,  12,  9 } };

        System.out.println( "is field cleared: " + isCleared( field ) );    // displays false
     */
        /*
        int[][] field = {
            {29,  29,  12,  11, 10 },
            {23,  24,  29,  12, 11 },
            {29,  12,  11,  13, 29 },
            {21,  11,  10,  12, 29 } };

        System.out.println( "is field cleared: " + isCleared( field ) );    // displays false 
        */
        /*
        int[][] field = {
            {29,  29,  12,  11, 10 },
            {13,  14,  29,  12, 11 },
            {29,  12,  11,  13, 29 },
            {11,  11,  10,  12, 29 } };

        System.out.println( "is field cleared: " + isCleared( field ) );    // displays true   
        */
        
        /*
        int[][] field = {
            { 9,  9,  2,  1,  0 },
            { 3,  4,  9,  2,  1 },
            { 9,  2,  1,  3,  9 },
            { 1,  1,  0,  2,  9 } };

        openMines( field );
        printTable( field ); 
        */
        
        /*
        int[][] field = new int[6][7];

        System.out.println("Is (3, 4) valid: " + isValid( field, 3, 4 ));    // displays true
        System.out.println("Is (7, 0) valid: " + isValid( field, 7, 0 ));    // displays false
        System.out.println("Is (-2, 4) valid: " + isValid( field, -2, 4 ));    // displays false
        System.out.println("Is (5, 8) valid: " + isValid( field, 5, 8 ));    // displays false
        */
        
        /*
        int[][] field = {
            { 9,  9,  0,  0,  0 },
            { 0,  0,  9,  0,  0 },
            { 9,  0,  0,  0,  9 },
            { 0,  0,  0,  0,  9 } };

        int mines = countMines( field, 1, 1 );
        System.out.println( "mines around cell (1,1): " + mines);    // displays 4

        mines = countMines( field, 0, 4 );
        System.out.println( "mines around cell (0,4): " + mines);    // displays 0
        */
        
        /*
        int[][] field = {
            { 9,  9,  0,  0,  0 },
            { 0,  0,  9,  0,  0 },
            { 9,  0,  0,  0,  9 },
            { 0,  0,  0,  0,  9 } };

        updateField( field );
        printTable( field );    
        */
        //int[][] field = generateField( 4, 5 );

        //printTable( field ); 
        
        /*
        int[][] field = {
            { 9,  9,  2,  1,  0 },
            { 3,  4,  9,  2,  1 },
            { 9,  2,  1,  3,  9 },
            { 1,  1,  0,  2,  9 } };

        //openCell( field, 1, 3 );
        //printTable( field );               
       
        openCell( field, 0, 4 );
        printTable( field );  
        */
        
        //drawCell( 50, 50, 7 );
        
        /*
        int[][] field = {
            { 9,  29,   2,   1, 20 },
            {23,  24,   9,  12,  1 },
            { 9,  12,  11,  13,  9 },
            { 1,  11,  10,  12,  9 } };

        drawField( field, 50, 150 );
        */
        playGame();
    }
}