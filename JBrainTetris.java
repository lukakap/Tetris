import javax.swing.*;
import java.awt.*;

public class JBrainTetris extends JTetris{

    private JCheckBox brainMode;
    private DefaultBrain dBrain;
    private int count;
    private Brain.Move move;
    private JPanel little;
    private JSlider adversary;
    private JLabel label;

    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        dBrain = new DefaultBrain();
        count = 0;
        move = new Brain.Move();
    }

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        JBrainTetris bTetris = new JBrainTetris(16);
        JFrame bFrame = JBrainTetris.createFrame(bTetris);
        bFrame.setVisible(true);
    }


    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel) super.createControlPanel();

        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        brainMode.setSelected(false);
        panel.add(brainMode);

        little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0); // min, max, current
        adversary.setPreferredSize(new Dimension(100,15));
        little.add(adversary);
        label = new JLabel("ok");
        little.add(label);
        panel.add(little);

        return panel;
    }

    @Override
    public void tick(int verb){
        if(brainMode.isSelected() && verb == super.DOWN && currentY < HEIGHT) {
            if(countChanged()) {
                this.count = super.count;
                board.undo();
                dBrain.bestMove(super.board,super.currentPiece,super.HEIGHT,this.move);
            }
            if(move != null) move();
        }
        super.tick(verb);
    }

    private void move() {
        if(!super.currentPiece.equals(move.piece)) super.tick(ROTATE);
        if(super.currentX < move.x) super.tick(RIGHT);
        if(super.currentX > move.x) super.tick(LEFT);
    }

    private boolean countChanged() {
        return (this.count != super.count);
    }

    @Override
    public Piece pickNextPiece() {
        int randBetween = random.nextInt(99) + 1;
        if(randBetween >= adversary.getValue()) {
            label.setText("ok");
            return super.pickNextPiece();
        } else {
            int pieceNumber = adversaryCompute();
            if(pieceNumber == -1) return super.pickNextPiece();
            label.setText("ok*");
            return pieces[pieceNumber];
        }
    }

    private int adversaryCompute() {
        Brain.Move movePeace = new Brain.Move();
        int worstScorePiece = -1;
        double worstScore = -1;
        for(int i = 0; i < pieces.length; i++) {
            dBrain.bestMove(board,pieces[i],HEIGHT,movePeace);
            if(movePeace != null) {
                if(movePeace.score > worstScore) {
                    worstScore = movePeace.score;
                    worstScorePiece = i;
                }
            }
        }
        return worstScorePiece;
    }

    //  ||Final Check OF Code||
}
