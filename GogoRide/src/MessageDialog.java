import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessageDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private MessageManager messageManager;
    private UserManager userManager;
    private UserProfile currentUser;
    private JList<UserProfile> userList;
    private JTextArea messageArea;
    private JTextArea composeArea;
    private DefaultListModel<UserProfile> userListModel;
    private UserProfile selectedUser;
    private SimpleDateFormat dateFormat;

    public MessageDialog(Frame parent, UserProfile currentUser, MessageManager messageManager, UserManager userManager) {
        super(parent, "Messages", true);
        this.currentUser = currentUser;
        this.messageManager = messageManager;
        this.userManager = userManager;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        setSize(600, 400);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Left panel with user list
        userListModel = new DefaultListModel<>();
        loadUsers();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedUser = userList.getSelectedValue();
                if (selectedUser != null) {
                    loadConversation();
                }
            }
        });

        JScrollPane userScrollPane = new JScrollPane(userList);
        userScrollPane.setPreferredSize(new Dimension(200, 0));
        add(userScrollPane, BorderLayout.WEST);

        // Right panel with messages and compose area
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Message display area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        rightPanel.add(messageScrollPane, BorderLayout.CENTER);

        // Compose panel
        JPanel composePanel = new JPanel(new BorderLayout());
        composeArea = new JTextArea(3, 20);
        composeArea.setLineWrap(true);
        composeArea.setWrapStyleWord(true);
        JScrollPane composeScrollPane = new JScrollPane(composeArea);
        composePanel.add(composeScrollPane, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        composePanel.add(sendButton, BorderLayout.EAST);

        rightPanel.add(composePanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            loadUsers();
            if (selectedUser != null) {
                loadConversation();
            }
        });
        add(refreshButton, BorderLayout.NORTH);
    }

    private void loadUsers() {
        userListModel.clear();
        // Add all users except current user
        for (UserProfile user : userManager.getAllUsers()) {
            if (!user.getUserId().equals(currentUser.getUserId())) {
                userListModel.addElement(user);
            }
        }
    }

    private void loadConversation() {
        if (selectedUser == null) return;

        List<Message> conversation = messageManager.getConversation(
            currentUser.getUserId(), 
            selectedUser.getUserId()
        );

        StringBuilder sb = new StringBuilder();
        for (Message message : conversation) {
            String sender = message.getSenderId().equals(currentUser.getUserId()) ? "You" : selectedUser.getFirstName();
            sb.append(String.format("[%s] %s: %s%n", 
                dateFormat.format(message.getTimestamp()),
                sender,
                message.getContent()
            ));
        }
        messageArea.setText(sb.toString());
        messageArea.setCaretPosition(messageArea.getDocument().getLength());

        // Mark messages as read
        messageManager.markAllMessagesAsRead(currentUser.getUserId());
    }

    private void sendMessage() {
        if (selectedUser == null) {
            JOptionPane.showMessageDialog(this,
                "Please select a user to send message to.",
                "Send Message",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String content = composeArea.getText().trim();
        if (content.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please enter a message.",
                "Send Message",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        messageManager.sendMessage(
            currentUser.getUserId(),
            selectedUser.getUserId(),
            content
        );

        composeArea.setText("");
        loadConversation();
    }
}
