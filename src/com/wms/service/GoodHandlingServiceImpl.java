package com.wms.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.wms.model.GRN;
import com.wms.model.GRN_Qty;
import com.wms.model.GRN_Show;
import com.wms.model.Item;
import com.wms.util.CommonConstants;
import com.wms.util.DBConnectionUtil;
import com.wms.util.QueryUtil;

public class GoodHandlingServiceImpl implements IGoodHandlingService {
	
	public static final Logger log = Logger.getLogger(IGoodHandlingService.class.getName());
	
	private static Connection connection;

	private static Statement statement;
	
	private PreparedStatement preparedStatement;
	
	static{
		//create table or drop if exist
		createTable();
	}
	
	private static void createTable() {

		try {
			connection = DBConnectionUtil.getDBConnection();
			statement = connection.createStatement();
			
			//Create Stored Procedure to drop tables 
			statement.executeUpdate(QueryUtil.queryByID(CommonConstants.QUERY_ID_DROP_TABLE_FUNCTION));
			
			// Drop table if already exists and as per SQL query available in

			statement.executeUpdate(QueryUtil.queryByID(CommonConstants.QUERY_ID_DROP_TABLE));
			
			// Create new employees table as per SQL query available in
			
			statement.executeUpdate(QueryUtil.queryByID(CommonConstants.QUERY_ID_CREATE_CUSTOMER_TABLE));
			statement.executeUpdate(QueryUtil.queryByID(CommonConstants.QUERY_ID_CREATE_GRN_TABLE));
			statement.executeUpdate(QueryUtil.queryByID(CommonConstants.QUERY_ID_CREATE_GRN_QTY_TABLE));
			statement.executeUpdate(QueryUtil.queryByID(CommonConstants.QUERY_ID_CREATE_ITEM_TABLE));
			

		} catch (SQLException | SAXException | IOException | ParserConfigurationException | ClassNotFoundException e) {
			
			log.log(Level.SEVERE, e.getMessage());
			
		} finally {
			
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage());
			}
			
		}
		
	}

	@Override
	public void addGRN(GRN grn) {
		
		try {
			
			connection = DBConnectionUtil.getDBConnection();
			preparedStatement = connection.prepareStatement(QueryUtil.queryByID(CommonConstants.QUERY_ID_INSERT_GRN));
			connection.setAutoCommit(false);
			
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_ONE, grn.getGRNNo());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_TWO, grn.getVehicleNo());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_THREE, grn.getContainerNo());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_FOUR, grn.getTrailerNo());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_FIVE, grn.getDate());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_SIX, grn.getsTime());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_SEVEN, grn.geteTime());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_EIGHT, grn.getCusId());
			
			preparedStatement.execute();
			connection.commit();
			
			
		} catch (Exception e) {
			
			log.log(Level.SEVERE, e.getMessage());
			
		} finally {
			
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
			
		}
		
	}
	
	@Override
	public ArrayList<GRN_Show> getGRNTable() {
		
		ArrayList<GRN_Show> grnlist = new ArrayList<GRN_Show>();
		
		try {
			
			connection = DBConnectionUtil.getDBConnection();
			preparedStatement = connection.prepareStatement(QueryUtil.queryByID(CommonConstants.QUERY_ID_GET_GRN_TABLE));
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				GRN_Show grn_Show = new GRN_Show();
				grn_Show.setCusName(resultSet.getString(CommonConstants.COLUMN_INDEX_ONE));
				grn_Show.setGRNNo(resultSet.getString(CommonConstants.COLUMN_INDEX_TWO));
				grn_Show.setItemName(resultSet.getString(CommonConstants.COLUMN_INDEX_THREE));
				grn_Show.setQty(resultSet.getFloat(CommonConstants.COLUMN_INDEX_FOUR));				
				grn_Show.setSqFeet(resultSet.getInt(CommonConstants.COLUMN_INDEX_FIVE));
				grn_Show.setwLoc(resultSet.getString(CommonConstants.COLUMN_INDEX_SIX));
				grn_Show.setDate(resultSet.getString(CommonConstants.COLUMN_INDEX_SEVEN));
				
				grnlist.add(grn_Show);
			}
			
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getMessage());
		} finally {
			
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
			
		}
				
		return grnlist;
	}

	@Override
	public GRN getGRNById(String GRNNo) {

		GRN_Show grn_Show = actionGRN(GRNNo).get(0);
		
		return grn_Show.getGRN();
	}
	
	@Override
	public ArrayList<GRN_Show> getGRNs() {
		
		return actionGRN(null);
	}
	
	private ArrayList<GRN_Show> actionGRN(String GRNNo){
		
		ArrayList<GRN_Show> grnlist = new ArrayList<GRN_Show>();
		
		try {
			
			connection = DBConnectionUtil.getDBConnection();
			
			if (GRNNo != null && !GRNNo.isEmpty()) {
				
				preparedStatement = connection.prepareStatement(QueryUtil.queryByID(CommonConstants.QUERY_ID_GET_GRN));
				preparedStatement.setString(CommonConstants.COLUMN_INDEX_ONE, GRNNo);
				
			}
			
			else {
				
				preparedStatement = connection.prepareStatement(QueryUtil.queryByID(CommonConstants.QUERY_ID_ALL_GRN));
				
			}
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				GRN grn = new GRN();
				grn.setGRNNo(resultSet.getString(CommonConstants.COLUMN_INDEX_ONE));
				grn.setVehicleNo(resultSet.getString(CommonConstants.COLUMN_INDEX_TWO));
				grn.setContainerNo(resultSet.getString(CommonConstants.COLUMN_INDEX_THREE));
				grn.setTrailerNo(resultSet.getString(CommonConstants.COLUMN_INDEX_FOUR));
				grn.setDate(resultSet.getString(CommonConstants.COLUMN_INDEX_FIVE));
				grn.setsTime(resultSet.getString(CommonConstants.COLUMN_INDEX_SIX));
				grn.seteTime(resultSet.getString(CommonConstants.COLUMN_INDEX_SEVEN));
				grn.setCusId(resultSet.getString(CommonConstants.COLUMN_INDEX_EIGHT));
				
				GRN_Qty grn_Qty = new GRN_Qty();
				grn_Qty.setId(resultSet.getInt(9));
				grn_Qty.setGRNNo(resultSet.getString(10));
				grn_Qty.setItemId(resultSet.getInt(11));
				grn_Qty.setQty(resultSet.getFloat(12));
				grn_Qty.setUom(resultSet.getString(13));
				grn_Qty.setSeqFeet(resultSet.getInt(14));
				grn_Qty.setCBM(resultSet.getInt(15));
				grn_Qty.setwLocId(resultSet.getString(16));
				grn_Qty.setDamageQty(resultSet.getFloat(17));
				grn_Qty.setStatus(resultSet.getString(18));
				grn_Qty.setRemark(resultSet.getString(19));
						
				
				grnlist.add(new GRN_Show(grn, grn_Qty));
								
			}
			
		} catch (Exception e) {
			
			log.log(Level.SEVERE, e.getMessage());
			
		} finally {
			
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		
		return grnlist;
		
	}

	@Override
	public void addGRNQty(GRN_Qty grn_Qty) {
		
		try {
			
			connection = DBConnectionUtil.getDBConnection();
			preparedStatement = connection.prepareStatement(QueryUtil.queryByID(CommonConstants.QUERY_ID_INSERT_GRN_QTY));
			connection.setAutoCommit(false);
			
			preparedStatement.setInt(CommonConstants.COLUMN_INDEX_ONE, grn_Qty.getId());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_TWO, grn_Qty.getGRNNo());
			preparedStatement.setInt(CommonConstants.COLUMN_INDEX_THREE, grn_Qty.getItemId());
			preparedStatement.setFloat(CommonConstants.COLUMN_INDEX_FOUR, grn_Qty.getQty());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_FIVE, grn_Qty.getUom());
			preparedStatement.setInt(CommonConstants.COLUMN_INDEX_SIX, grn_Qty.getSeqFeet());
			preparedStatement.setInt(CommonConstants.COLUMN_INDEX_SEVEN, grn_Qty.getCBM());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_EIGHT, grn_Qty.getwLocId());
			preparedStatement.setFloat(CommonConstants.COLUMN_INDEX_NINE, grn_Qty.getDamageQty());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_TEN, grn_Qty.getStatus());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_ELEVEN, grn_Qty.getRemark());
			
			preparedStatement.execute();
			connection.commit();
	
		} catch (Exception e) {
			
			log.log(Level.SEVERE, e.getMessage());
			
		} finally {
			
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
			
	}

	@Override
	public void addItem(Item item) {
		
		try {
			
			connection = DBConnectionUtil.getDBConnection();
			preparedStatement = connection.prepareStatement(QueryUtil.queryByID(CommonConstants.QUERY_ID_INSERT_ITEM));
			connection.setAutoCommit(false);
			
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_ONE, item.getItemName());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_TWO, item.getItemDes());
			preparedStatement.setString(CommonConstants.COLUMN_INDEX_THREE, item.getRemark());
			
			preparedStatement.execute();
			connection.commit();
			
		} catch (Exception e) {
			
			log.log(Level.SEVERE, e.getMessage());
			
		} finally {
			
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		
	}

	@Override
	public int getItemCode() {
		
		int itemCount = 0;
		
		try {
			
			connection = DBConnectionUtil.getDBConnection();
			
			preparedStatement = connection.prepareStatement(QueryUtil.queryByID(CommonConstants.QUERY_ID_GET_ITEM_CODE));
			
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				
				itemCount = resultSet.getInt(CommonConstants.COLUMN_INDEX_ONE);
				
			}
			
		} catch (Exception e) {
			
			log.log(Level.SEVERE, e.getMessage());
			
		} finally {
			
			try {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				log.log(Level.SEVERE, e.getMessage());
			}
		}
		
		return ++itemCount;
	}


}
