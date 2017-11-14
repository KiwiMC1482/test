/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package draughtscoursework;

/**
 *
 * @author user
 */
public enum MoveStatus 
{
    DONE 
    {
        @Override
        boolean isDone() 
        {
            return true;
        }
    },
    ILLEGAL {
        @Override
        boolean isDone() 
        {
            return false;
        }
    };
    
    
    abstract boolean isDone();
}
