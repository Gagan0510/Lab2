/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * File Name: IndyWinner
 * Author: Gagandeep kaur Sangha, ID 041004212
 * Course: CST8288
 * Lab:2
 * Date:11/17/2024
 * Professor: Sazzad Hossain
*/
public class IndyWinner {
    private int year;        // The year 
    private String driver;   // The name of the driver 
    private double averageSpeed;  // The average speed 
    private String country;   // The country 

    /**
     * Constructor for the IndyWinner class.
     *
     */
    public IndyWinner(int year, String driver, double averageSpeed, String country) {
        this.year = year;
        this.driver = driver;
        this.averageSpeed = averageSpeed;
        this.country = country;
    }
// Getters and Setters
    public int getYear() {
        return year;
    }

    public String getDriver() {
        return driver;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public String getCountry() {
        return country;
    }
}

