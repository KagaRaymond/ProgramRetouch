package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import base.DBManager;
import beans.BuyDataBeans;
import beans.BuyDetailDataBeans;
import beans.DeliveryMethodDataBeans;
import beans.ItemDataBeans;

/**
 *
 * @author d-yamaguchi
 *
 */
public class BuyDetailDAO {

	/**
	 * 購入詳細登録処理
	 * @param bddb BuyDetailDataBeans
	 * @throws SQLException
	 * 			呼び出し元にスローさせるため
	 */
	public static void insertBuyDetail(BuyDetailDataBeans bddb) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();
			st = con.prepareStatement(
					"INSERT INTO t_buy_detail(buy_id,item_id) VALUES(?,?)");
			st.setInt(1, bddb.getBuyId());
			st.setInt(2, bddb.getItemId());
			st.executeUpdate();
			System.out.println("inserting BuyDetail has been completed");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * 購入IDによる購入情報検索
	 * @param buyId
	 * @return {BuyDataDetailBeans}
	 * @throws SQLException
	 */
	public ArrayList<BuyDetailDataBeans> getBuyDataBeansListByBuyId(int buyId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement("SELECT * FROM t_buy_detail WHERE buy_id = ?");
			st.setInt(1, buyId);

			ResultSet rs = st.executeQuery();
			ArrayList<BuyDetailDataBeans> buyDetailList = new ArrayList<BuyDetailDataBeans>();

			while (rs.next()) {
				BuyDetailDataBeans bddb = new BuyDetailDataBeans();
				bddb.setId(rs.getInt("id"));
				bddb.setBuyId(rs.getInt("buy_id"));
				bddb.setItemId(rs.getInt("item_id"));
				buyDetailList.add(bddb);
			}

			System.out.println("searching BuyDataBeansList by BuyID has been completed");
			return buyDetailList;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	 /**
     * 購入IDによる購入詳細情報検索
     * @param buyId
     * @return buyDetailItemList ArrayList<ItemDataBeans>
     *             購入詳細情報のデータを持つJavaBeansのリスト
     * @throws SQLException
     */
	public static ArrayList<ItemDataBeans> getItemDataBeansListByBuyId(int buyId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement(
					"SELECT m_item.id,"
					+ " m_item.name,"
					+ " m_item.price"
					+ " FROM t_buy_detail"
					+ " JOIN m_item"
					+ " ON t_buy_detail.item_id = m_item.id"
					+ " WHERE t_buy_detail.buy_id = ?");
			st.setInt(1, buyId);

			ResultSet rs = st.executeQuery();
			ArrayList<ItemDataBeans> buyDetailItemList = new ArrayList<ItemDataBeans>();

			while (rs.next()) {
				ItemDataBeans idb = new ItemDataBeans();
				idb.setId(rs.getInt("id"));
				idb.setName(rs.getString("name"));
				idb.setPrice(rs.getInt("price"));


				buyDetailItemList.add(idb);
			}

			System.out.println("searching ItemDataBeansList by BuyID has been completed");
			return buyDetailItemList;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	 /**
     * ユーザIDによる購入詳細情報取得
     * @param buyId
     * @return buyDetailItemList ArrayList<ItemDataBeans>
     *             購入詳細情報のデータを持つJavaBeansのリスト
     * @throws SQLException
     */

	public static ArrayList<ItemDataBeans> getItemDataBeansListByUserId(int userId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = DBManager.getConnection();

			st = con.prepareStatement(
					"SELECT t_buy.create_date,"
					+ " t_buy.total_price,"
					+ " m_delivery_method.name,"
					+ " m_delivery_method.price,"
					+ " m_item.name,"
					+ " m_item.price"
					+ " FROM t_user"
					+ " INNER JOIN t_buy"
					+ " ON t_user.id = t_buy.user_id"
					+ " INNER JOIN t_buy_detail"
					+ " ON t_buy.id = t_buy_detail.id"
					+ " INNER JOIN m_delivery_method"
					+ " ON t_buy.delivery_method_id = m_delivery_method.id"
					+ " INNER JOIN m_item"
					+ " ON t_buy_detail.item_id = m_item.id"
					+ " WHERE t_user.id = ?");

			st.setInt(1, userId);

			ResultSet rs = st.executeQuery();
			ArrayList<ItemDataBeans> buyDetailItemList = new ArrayList<ItemDataBeans>();

			while (rs.next()) {
				ItemDataBeans idb = new ItemDataBeans();
				BuyDataBeans bdb = new BuyDataBeans();
				DeliveryMethodDataBeans dmdb = new DeliveryMethodDataBeans();

				bdb.setBuyDate(rs.getDate("create_date"));
				bdb.setDeliveryMethodName(rs.getString("deliveryMethodName"));
				idb.setId(rs.getInt("id"));
				idb.setName(rs.getString("name"));
				idb.setPrice(rs.getInt("price"));


				buyDetailItemList.add(idb);
			}

			System.out.println("searching ItemDataBeansList by BuyID has been completed");
			return buyDetailItemList;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	/**
	 * 課題2 ユーザーデータ画面　購入履歴表示機能　
	 * ユーザIDによる購入履歴
	 * 購入日時、配達方法、購入金額
	**/

	public static ArrayList<BuyDataBeans> getBuyDataBeansByUserId(int userId) throws SQLException {
		Connection con = null;
		PreparedStatement st = null;
		ArrayList<BuyDataBeans> buyDetailBuyItemList = new ArrayList<BuyDataBeans>();

		try {
			con = DBManager.getConnection();

			st = con.prepareStatement(
					"SELECT * FROM t_buy"
							+ " JOIN m_delivery_method"
							+ " ON t_buy.delivery_method_id = m_delivery_method.id"
							+ " JOIN t_buy_detail"
							+ " ON t_buy.id = t_buy_detail.buy_id"
							+ " WHERE t_buy.user_id = ?"
							+ " ORDER BY create_date DESC");
			st.setInt(1, userId);

			ResultSet rs = st.executeQuery();


			while (rs.next()) {
				BuyDataBeans userbdb = new BuyDataBeans();
				userbdb.setTotalPrice(rs.getInt("total_price"));
				userbdb.setBuyDate(rs.getTimestamp("create_date"));
				userbdb.setDeliveryMethodPrice(rs.getInt("price"));
				userbdb.setDeliveryMethodName(rs.getString("name"));

				buyDetailBuyItemList.add(userbdb);
			}

			System.out.println("searching BuyDataBeans by buyID has been completed");

			return buyDetailBuyItemList;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new SQLException(e);
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}





}
