import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;


public class LibraryManagementSystemGUI extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost/Library";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin";

    private JTextField isbnTextField;
    private JTextField titleTextField;
    private JTextField authorsTextField;
    private JTextField publisherTextField;
    private JTextField publicationDateTextField;
    private JTextField typeTextField;
    private JTextArea resultTextArea;
    private JTable resultTable;
    private JPanel searchPanel;
    private JPanel borrowPanel;
    private JPanel returnPanel;
    private JScrollPane scrollPane;
    public LibraryManagementSystemGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        searchPanel = createSearchPanel();
        borrowPanel = createBorrowPanel();
        returnPanel = createReturnPanel();

        add(searchPanel, BorderLayout.NORTH);
        add(borrowPanel, BorderLayout.CENTER);
        add(returnPanel, BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel isbnLabel = new JLabel("ISBN:");
        isbnTextField = new JTextField(15);
        panel.add(isbnLabel);
        panel.add(isbnTextField);

        JLabel titleLabel = new JLabel("Title:");
        titleTextField = new JTextField(15);
        panel.add(titleLabel);
        panel.add(titleTextField);

        JLabel authorsLabel = new JLabel("Authors:");
        authorsTextField = new JTextField(15);
        panel.add(authorsLabel);
        panel.add(authorsTextField);

        JLabel publisherLabel = new JLabel("Publisher:");
        publisherTextField = new JTextField(15);
        panel.add(publisherLabel);
        panel.add(publisherTextField);

        JLabel publicationDateLabel = new JLabel("Publication Date:");
        publicationDateTextField = new JTextField(15);
        panel.add(publicationDateLabel);
        panel.add(publicationDateTextField);

        JLabel typeLabel = new JLabel("Type:");
        typeTextField = new JTextField(15);
        panel.add(typeLabel);
        panel.add(typeTextField);

        JButton searchButton = new JButton("Search");

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ISBN");
        tableModel.addColumn("Title");
        tableModel.addColumn("Authors");
        tableModel.addColumn("Publisher");
        tableModel.addColumn("Edition Number");
        tableModel.addColumn("Publication Date");
        tableModel.addColumn("Type");
        // 创建表格并设置模型
        resultTable = new JTable(tableModel);
        resultTable.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(resultTable);
        panel.add(searchButton);
        panel.add(scrollPane);

        // 添加搜索按钮的点击事件监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });

        // 创建表格模型
//        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        panel.add(searchButton);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel createBorrowPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel borrowIsbnLabel = new JLabel("ISBN:");
        JTextField borrowIsbnTextField = new JTextField(15);
        JLabel borrowReaderIdLabel = new JLabel("Reader ID:");
        JTextField borrowReaderIdTextField = new JTextField(15);
        JLabel borrowReturnDateLabel = new JLabel("ReturnDate:");
        JTextField borrowReturnDateTextField = new JTextField(15);

        JButton borrowButton = new JButton("Borrow");

        panel.add(borrowIsbnLabel);
        panel.add(borrowIsbnTextField);
        panel.add(borrowReaderIdLabel);
        panel.add(borrowReaderIdTextField);
        panel.add(borrowReturnDateLabel);
        panel.add(borrowReturnDateTextField);
        panel.add(borrowButton);

        // 添加借阅按钮的点击事件监听器
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String isbn = borrowIsbnTextField.getText();
                int readerId = Integer.parseInt(borrowReaderIdTextField.getText());
                Date returnDate = Date.valueOf(borrowReturnDateTextField.getText());
//                System.out.println(returnDate);
                borrowBook(isbn, readerId , returnDate);
            }
        });

        return panel;
    }

    private JPanel createReturnPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());


        JLabel returnISBNLabel = new JLabel("ISBN:");
        JTextField returnISBNTextField = new JTextField(15);

        JLabel returnReaderIdLabel = new JLabel("Reader ID:");
        JTextField returnReaderIdTextField = new JTextField(15);

        JButton returnButton = new JButton("Return");


        panel.add(returnISBNLabel);
        panel.add(returnISBNTextField);
        panel.add(returnReaderIdLabel);
        panel.add(returnReaderIdTextField);
        panel.add(returnButton);

        // 添加归还按钮的点击事件监听器
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int readerId = Integer.parseInt(returnReaderIdTextField.getText());
                String isbn = returnISBNTextField.getText();
                returnBook(isbn,readerId);
            }
        });

        return panel;
    }

    private void searchBooks() {
        String isbn = isbnTextField.getText();
        String title = titleTextField.getText();
        String authors = authorsTextField.getText();
        String publisher = publisherTextField.getText();
        String publicationDate = publicationDateTextField.getText();
        String type = typeTextField.getText();

        // 构建查询SQL语句_
        String sql = "SELECT * FROM Books WHERE ISBN = ? AND Title LIKE ? AND Authors LIKE ? " +
                "AND Publisher LIKE ? AND PublicationDate = ? AND Type LIKE ?";

        // 构建查询SQL语句
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Books WHERE 1=1");
        if (!isbn.isEmpty()) {
            queryBuilder.append(" AND ISBN = '").append(isbn).append("'");
        }
        if (!title.isEmpty()) {
            queryBuilder.append(" AND Title LIKE '%").append(title).append("%'");
        }
        if (!authors.isEmpty()) {
            queryBuilder.append(" AND Authors LIKE '%").append(authors).append("%'");
        }
        if (!publisher.isEmpty()) {
            queryBuilder.append(" AND Publisher LIKE '%").append(publisher).append("%'");
        }
        if (!publicationDate.isEmpty()) {
            queryBuilder.append(" AND PublicationDate = '").append(publicationDate).append("'");
        }
        if (!type.isEmpty()) {
            queryBuilder.append(" AND Type LIKE '%").append(type).append("%'");
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryBuilder.toString())) {

//            // 设置查询条件的参数
//            statement.setString(1, isbn);
//            statement.setString(2, title != null ? "%" + title + "%" : "%");
//            statement.setString(3, authors != null ? "%" + authors + "%" : "%");
//            statement.setString(4, publisher != null ? "%" + publisher + "%" : "%");
//            statement.setString(5, publicationDate != null ? publicationDate : "%");
//            statement.setString(6, type != null ? "%" + type + "%" : "%");


//            // 构建查询结果字符串
//            StringBuilder resultBuilder = new StringBuilder();

            // 执行查询
//            ResultSet resultSet = statement.executeQuery();

            // 构建表格模型
            DefaultTableModel tableModel = (DefaultTableModel) resultTable.getModel();
            tableModel.setNumRows(0);
//            tableModel.addColumn("ISBN");
//            tableModel.addColumn("Title");
//            tableModel.addColumn("Authors");
//            tableModel.addColumn("Publisher");
//            tableModel.addColumn("Edition Number");
//            tableModel.addColumn("Publication Date");
//            tableModel.addColumn("Type");

            // 填充表格模型数据
            while (resultSet.next()) {
                String bookISBN = resultSet.getString("ISBN");
                String bookTitle = resultSet.getString("Title");
                String bookAuthors = resultSet.getString("Authors");
                String bookPublisher = resultSet.getString("Publisher");
                int editionNumber = resultSet.getInt("EditionNumber");
                Date publicationDateResult = resultSet.getDate("PublicationDate");
                String bookType = resultSet.getString("Type");

                Object[] rowData = {
                        bookISBN, bookTitle, bookAuthors, bookPublisher, editionNumber, publicationDateResult, bookType
                };
                tableModel.addRow(rowData);
            }

            // 创建表格并设置模型
//            resultTable = new JTable(tableModel);
//
//            // 创建滚动面板，并将表格添加到面板中
//            JScrollPane scrollPaneNew = new JScrollPane(resultTable);
//
//            // 移除旧的结果文本区域，将滚动面板添加到搜索面板中
//            searchPanel.remove(scrollPane);
//
//            searchPanel.add(scrollPaneNew);
//
//            // 重新绘制界面
//            searchPanel.revalidate();
//            searchPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 借书
    private void borrowBook(String isbn, int readerId,Date returnDate) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // 检查书籍是否已经被借出
            String checkAvailabilitySql = "SELECT * FROM Record WHERE ISBN = ? ";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkAvailabilitySql)) {
                checkStatement.setString(1, isbn);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    returnDate = resultSet.getDate("ReturnDate");
                    JOptionPane.showMessageDialog(this, "该书已被借出，归还时间：" + returnDate);
                } else {
                    // 检查读者权限
                    String checkLimitsSql = "SELECT Limits FROM Reader WHERE ReaderID = ?";
                    try (PreparedStatement limitsStatement = connection.prepareStatement(checkLimitsSql)) {
                        limitsStatement.setInt(1, readerId);
                        ResultSet limitsResultSet = limitsStatement.executeQuery();

                        if (limitsResultSet.next()) {
                            int limits = limitsResultSet.getInt("Limits");
                            int borrowedBooks = getBorrowedBooksCount(connection, readerId);

                            if (borrowedBooks < limits) {
                                // 执行借书操作
                                String borrowSql = "INSERT INTO Record (ISBN, ReaderID, BorrowingDate,ReturnDate) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement borrowStatement = connection.prepareStatement(borrowSql)) {
                                    borrowStatement.setString(1, isbn);
                                    borrowStatement.setInt(2, readerId);
                                    borrowStatement.setDate(3, new Date(System.currentTimeMillis()));
                                    borrowStatement.setDate(4, returnDate);
                                    borrowStatement.executeUpdate();
                                    JOptionPane.showMessageDialog(this, "借书成功！");
                                }
                            } else {
                                JOptionPane.showMessageDialog(this, "已超过您的最大借阅数目");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 获取读者已借阅书籍数量
    private int getBorrowedBooksCount(Connection connection, int readerId) throws SQLException {
        String countSql = "SELECT COUNT(*) AS Count FROM Record WHERE ReaderID = ? ";
        try (PreparedStatement countStatement = connection.prepareStatement(countSql)) {
            countStatement.setInt(1, readerId);
            ResultSet resultSet = countStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Count");
            }
        }
        return 0;
    }

    // 还书
    private void returnBook(String isbn,int readerId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateSql = "UPDATE Record SET ReturnDate = ? WHERE ReaderID = ? AND ISBN =?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                updateStatement.setDate(1, new Date(System.currentTimeMillis()));
                updateStatement.setInt(2, readerId);
                updateStatement.setString(3,isbn);
                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    // 执行书操作
                    String borrowSql = "DELETE FROM record WHERE ISBN = ? AND ReaderID = ?";
                    try (PreparedStatement borrowStatement = connection.prepareStatement(borrowSql)) {
                        borrowStatement.setString(1, isbn);
                        borrowStatement.setInt(2, readerId);
                        borrowStatement.executeUpdate();
                        JOptionPane.showMessageDialog(this, "还书成功！");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "无法找到借阅记录");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除借阅记录
    private void deleteRecord(Connection connection, int recordId) throws SQLException {
        String deleteSql = "DELETE FROM Record WHERE RecordID = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setInt(1, recordId);
            deleteStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LibraryManagementSystemGUI gui = new LibraryManagementSystemGUI();
                gui.setVisible(true);
            }
        });
    }
}

