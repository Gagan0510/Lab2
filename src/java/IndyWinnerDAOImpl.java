/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * File Name: IndyWinnerDAOImpl
 * Author: Gagandeep kaur Sangha, ID 041004212
 * Course: CST8288
 * Lab:2
 * Date:11/17/2024
 * Professor: Sazzad Hossain
*/
//package com.algonquin.cst8288.dao;

//package com.algonquin.cst8288.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gagsa
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IndyWinnerDAOImpl implements IndyWinnerDAO {

    @Override
    public List<IndyWinner> getWinners(int offset, int limit) throws Exception {
        List<IndyWinner> winners = new ArrayList<>();
        String query = "SELECT * FROM INDYWINNERS ORDER BY YEAR DESC LIMIT ? OFFSET ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, limit);
            statement.setInt(2, offset);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    IndyWinner winner = new IndyWinner(
                            resultSet.getInt("YEAR"),
                            resultSet.getString("DRIVER"),
                            resultSet.getDouble("AVERAGESPEED"),
                            resultSet.getString("COUNTRY")
                    );
                    winners.add(winner);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error fetching winners: " + e.getMessage(), e);
        }
        return winners;
    }
}
