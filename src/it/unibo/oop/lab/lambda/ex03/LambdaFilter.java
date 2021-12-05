package it.unibo.oop.lab.lambda.ex03;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Modify this small program adding new filters.
 * Realize this exercise using as much as possible the Stream library.
 * 
 * 1) Convert to lowercase
 * 
 * 2) Count the number of chars
 * 
 * 3) Count the number of lines
 * 
 * 4) List all the words in alphabetical order
 * 
 * 5) Write the count for each word, e.g. "word word pippo" should output "pippo -> 1 word -> 2"
 *
 */
public final class LambdaFilter extends JFrame {

    /**
     * ASCII value of character "A".
     */
    public static final int UPPERA = 65;
    /**
     * ASCII value of character "Z".
     */
    public static final int UPPERZ = 90;
    /**
     * ASCII value of character "a".
     */
    public static final int LOWERA = 97;

    private static final long serialVersionUID = 1760990730218643730L;

    private enum Command {
        IDENTITY("No modifications", Function.identity()),
        TOLOWERCASE("Lower case", str -> str.chars()
                                            .map(ch -> (ch >= UPPERA && ch <= UPPERZ) ? ch + LOWERA - UPPERA : ch)
                                            .collect(StringBuilder::new, (s, c) -> s.append((char) c), StringBuilder::append).toString()),
        NUMCHARS("Number of characters", str -> Long.toString(str.chars().count())),
        NUMLINES("Number of lines", str -> str.equals("") ? "0" : Long.toString(str.chars()
                                                                                .filter(ch -> ch == '\n')
                                                                                .count() + 1)),
        ORDEREDWORDS("Order words alphabetically", str -> Arrays.asList(str.split(" "))
                                                                .stream()
                                                                .sorted(String::compareTo)
                                                                .collect(Collectors.joining(" "))),
        WORDCOUNT("Count for each word", str -> Arrays.asList(str.split(" "))
                .stream()
                .collect(Collectors.groupingBy(s -> s, HashMap::new, Collectors.mapping(s -> s, Collectors.counting())))
                .entrySet().stream()
                .map(e -> e.getKey() + " -> " + e.getValue())
//                .reduce("", (s1, s2) -> s1.concat(s2 + ", "))
                .collect(Collectors.joining(", ")));

        private final String commandName;
        private final Function<String, String> fun;

        Command(final String name, final Function<String, String> process) {
            commandName = name;
            fun = process;
        }

        @Override
        public String toString() {
            return commandName;
        }

        public String translate(final String s) {
            return fun.apply(s);
        }
    }

    private LambdaFilter() {
        super("Lambda filter GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel1 = new JPanel();
        final LayoutManager layout = new BorderLayout();
        panel1.setLayout(layout);
        final JComboBox<Command> combo = new JComboBox<>(Command.values());
        panel1.add(combo, BorderLayout.NORTH);
        final JPanel centralPanel = new JPanel(new GridLayout(1, 2));
        final JTextArea left = new JTextArea();
        left.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        final JTextArea right = new JTextArea();
        right.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        right.setEditable(false);
        centralPanel.add(left);
        centralPanel.add(right);
        panel1.add(centralPanel, BorderLayout.CENTER);
        final JButton apply = new JButton("Apply");
        apply.addActionListener(ev -> right.setText(((Command) combo.getSelectedItem()).translate(left.getText())));
        panel1.add(apply, BorderLayout.SOUTH);
        setContentPane(panel1);
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int sw = (int) screen.getWidth();
        final int sh = (int) screen.getHeight();
        setSize(sw / 4, sh / 4);
        setLocationByPlatform(true);
    }

    /**
     * @param a unused
     */
    public static void main(final String... a) {
        final LambdaFilter gui = new LambdaFilter();
        gui.setVisible(true);
    }
}
