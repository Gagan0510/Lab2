/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * File Name: IndyWinnerDAO
 * Author: Gagandeep kaur Sangha, ID 041004212
 * Course: CST8288
 * Lab:2
 * Date:11/17/2024
 * Professor: Sazzad Hossain
*/

/**
 * The IndyWinnerDAO interface defines the data access methods for retrieving 
 * information by intereaction with database
 */
import java.util.List;


public interface IndyWinnerDAO {
    List<IndyWinner> getWinners(int offset, int limit) throws Exception;
}

