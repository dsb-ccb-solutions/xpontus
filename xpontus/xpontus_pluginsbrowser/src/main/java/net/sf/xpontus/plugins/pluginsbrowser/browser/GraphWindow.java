// Placed in public domain by Dmitry Olshansky, 2006
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginPrerequisite;
import org.java.plugin.registry.PluginRegistry;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeShapeFunction;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.utils.GraphUtils;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.visualization.AbstractRenderer;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.HasShapeFunctions;
import edu.uci.ics.jung.visualization.ISOMLayout;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

final class GraphWindow extends JDialog {
    private static final long serialVersionUID = -1727813017432133164L;

    static final int INSET = 10;

    private JPanel jContentPane = null;
    VisualizationViewer vv;
    private JLabel infoLabel;
    JCheckBox corner;
    
    GraphWindow(final Frame owner) {
        super(owner);
        initialize();
    }
    
    void setRegistry(final PluginRegistry registry) throws Exception {
        CustomRenderer renderer = new CustomRenderer();
        //KKLayout layout = new KKLayout(buildGraph(registry));
        //FRLayout layout = new FRLayout(buildGraph(registry));
        //layout.setMaxIterations(10);
        //DAGLayout layout = new DAGLayout(buildGraph(registry));
        ISOMLayout layout = new ISOMLayout(buildGraph(registry));
        vv = new VisualizationViewer(layout, renderer, getSize());
        vv.setPickSupport(new ShapePickSupport(vv, vv, renderer, 2));
        vv.setGraphMouse(new DefaultModalGraphMouse());
        vv.setFont(new Font("Arial", Font.BOLD, 18));
        GraphZoomScrollPane scrollPane = new GraphZoomScrollPane(vv);
        corner = new JCheckBox("CORNER");
        corner.addChangeListener(new ChangeListener() {
            public void stateChanged(final ChangeEvent evt) {
                setMouseMode();
            }
        });
        setMouseMode();
        scrollPane.setCorner(corner);
        getJContentPane().add(scrollPane, BorderLayout.CENTER);
    }
    
    void setMouseMode() {
        if (corner.isSelected()) {
            ((DefaultModalGraphMouse) vv.getGraphMouse()).setMode(Mode.PICKING);
            corner.setToolTipText("Current mode is: picking.");
            getInfoLabel().setText("Current mode is: picking. Use mouse to select and move nodes. Use SHIFT and CTRL keys to alternate behavior.");
        } else {
            ((DefaultModalGraphMouse) vv.getGraphMouse()).setMode(
                    Mode.TRANSFORMING);
            corner.setToolTipText("Current mode is: transforming.");
            getInfoLabel().setText("Current mode is: transforming. Use mouse to move diagram. Use wheel to zoom in/out. Use SHIFT and CTRL keys to alternate behavior.");
        }
    }
    
    private Graph buildGraph(final PluginRegistry registry) throws Exception {
        Graph result = new DirectedSparseGraph();
        StringLabeller sl = StringLabeller.getLabeller(result);
        for (Iterator it = registry.getPluginDescriptors().iterator();
                it.hasNext();) {
            PluginDescriptor descr = (PluginDescriptor) it.next();
            sl.setLabel(result.addVertex(new SparseVertex()), descr.getId());
        }
        for (Iterator it = registry.getPluginDescriptors().iterator();
                it.hasNext();) {
            PluginDescriptor descr = (PluginDescriptor) it.next();
            Vertex v1 = sl.getVertex(descr.getId());
            for (Iterator it2 = descr.getPrerequisites().iterator();
                    it2.hasNext();) {
                PluginPrerequisite pre = (PluginPrerequisite) it2.next();
                Vertex v2 = sl.getVertex(pre.getPluginId());
                if (v2 == null) {
                    // missing prerequisite!
                    v2 = result.addVertex(new SparseVertex());
                    sl.setLabel(v2, pre.getPluginId());
                    v2.setUserDatum("absent", "", UserData.SHARED);
                }
                GraphUtils.addEdge(result, v2, v1);
            }
        }
        return result;
    }

    private void initialize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(screenSize.width - 50, screenSize.height - 100);
        this.setTitle("Plug-ins Structure");
        this.setModal(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJContentPane());
    }

    private JLabel getInfoLabel() {
        if (infoLabel == null) {
            infoLabel = new JLabel();
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            getJContentPane().add(infoLabel, BorderLayout.SOUTH);
            infoLabel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(final MouseEvent evt) {
                    corner.setSelected(!corner.isSelected());
                }
            });
        }
        return infoLabel;
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
        }
        return jContentPane;
    }
    
    private final class CustomRenderer extends AbstractRenderer
            implements HasShapeFunctions {
        CustomRenderer() {
            // no-op
        }
        
        private VertexShapeFunction vertexShapeFunction =
                new VertexShapeFunction() {
            public Shape getShape(final Vertex v) {
                Graphics graphics = vv.getGraphics();
                Font font = graphics.getFont();
                try {
                    //graphics.setFont(FONT);
                    Rectangle2D bounds =
                        graphics.getFontMetrics().getStringBounds(
                            StringLabeller.getLabeller(
                                    (Graph) v.getGraph()).getLabel(v),
                                    graphics);
                    int width = (int) Math.floor(bounds.getWidth()) + INSET * 2;
                    int height = (int) Math.floor(bounds.getHeight()) + INSET * 2;
                    return new Rectangle(- width / 2, - height / 2,
                            width, height);
                } finally {
                    graphics.setFont(font);
                }
            }
        };
        private EdgeShapeFunction edgeShapeFunction = new EdgeShapeFunction() {
            public Shape getShape(final Edge e) {
                return new Line2D.Double(0, 0, 0, 0);
            }
        };
        
        /**
         * @see edu.uci.ics.jung.visualization.AbstractRenderer#paintEdge(
         *      java.awt.Graphics, edu.uci.ics.jung.graph.Edge,
         *      int, int, int, int)
         */
        public void paintEdge(final Graphics gr, final Edge e, final int x1,
                final int y1, final int x2, final int y2) {
            Vertex v1 = (Vertex) e.getEndpoints().getFirst();
            Vertex v2 = (Vertex) e.getEndpoints().getSecond();
            Rectangle rect1 = (Rectangle) vertexShapeFunction.getShape(v1);
            rect1.setLocation(x1 - rect1.width / 2, y1 -  rect1.height / 2);
            Rectangle rect2 = (Rectangle) vertexShapeFunction.getShape(v2);
            rect2.setLocation(x2 - rect2.width / 2, y2 - rect2.height / 2);
            if (rect1.intersects(rect2)) {
                return;
            }
            Line2D line = new Line2D.Double(
                    getIntersection(rect1, rect2.getCenterX(), rect2.getCenterY()),
                    getIntersection(rect2, rect1.getCenterX(), rect1.getCenterY()));
            if ((line.getX1() == line.getX2())
                    && (line.getY1() == line.getY2())) {
                return;
            }
            if (isPicked(v1) || isPicked(v2)) {
                gr.setColor(Color.BLUE);
            } else {
                gr.setColor(Color.BLACK);
            }
            gr.drawLine((int) line.getX1(), (int) line.getY1(),
                    (int) line.getX2(), (int) line.getY2());
            // painting arrow
            GeneralPath arrow = new GeneralPath();
            arrow.moveTo(0f, 0f);
            arrow.lineTo(-13.0f, 5.0f);
            arrow.lineTo(-13.0f, -5.0f);
            arrow.lineTo(0f, 0f);
            arrow.transform(AffineTransform.getRotateInstance(Math.atan(
                    (line.getP1().getX() - line.getP2().getX()) /
                    (line.getP2().getY() - line.getP1().getY())) + Math.PI / 2.0
                    * (line.getP1().getY() <= line.getP2().getY() ? -1 : 1)));
            arrow.transform(AffineTransform.getTranslateInstance(
                    line.getP1().getX(), line.getP1().getY()));
            ((Graphics2D) gr).fill(arrow);
        }
        
        private Point2D getIntersection(final Rectangle rect, final double x,
                final double y) {
            double cx = rect.getCenterX();
            double cy = rect.getCenterY();
            if (Line2D.linesIntersect(cx, cy, x, y, rect.x, rect.y,
                    rect.x + rect.width, rect.y)) {
                return new Point2D.Double(
                        cx - (cx - x) / (cy - y) * rect.height / 2.0,
                        cy - rect.height / 2.0);
            }
            if (Line2D.linesIntersect(cx, cy, x, y, rect.x,
                    rect.y + rect.height, rect.x + rect.width,
                    rect.y + rect.height)) {
                return new Point2D.Double(
                        cx + (cx - x) / (cy - y) * rect.height / 2.0,
                        cy + rect.height / 2.0);
            }
            if (Line2D.linesIntersect(cx, cy, x, y, rect.x, rect.y, rect.x,
                    rect.y + rect.height)) {
                return new Point2D.Double(cx - rect.width / 2.0,
                        cy - (cy - y) / (cx - x) * rect.width / 2.0);
            }
            return new Point2D.Double(cx + rect.width / 2.0,
                    cy + (cy - y) / (cx - x) * rect.width / 2.0);
        }

        /**
         * @see edu.uci.ics.jung.visualization.AbstractRenderer#paintVertex(
         *      java.awt.Graphics, edu.uci.ics.jung.graph.Vertex, int, int)
         */
        public void paintVertex(final Graphics gr, final Vertex v,
                final int x, final int y) {
            Graphics graphics = getGraphics();
            Font font = graphics.getFont();
            try {
                if (isPicked(v)) {
                    gr.setColor(Color.GRAY);
                } else {
                    gr.setColor(Color.LIGHT_GRAY);
                }
                Rectangle rect = (Rectangle) vertexShapeFunction.getShape(v);
                gr.fillRoundRect(x - rect.width / 2, y - rect.height / 2,
                        rect.width, rect.height, 7, 7);
                gr.setColor(Color.BLACK);
                if (v.containsUserDatumKey("absent")) {
                    gr.setColor(Color.RED);
                } else {
                    gr.setColor(Color.BLACK);
                }
                gr.drawRoundRect(x - rect.width / 2, y - rect.height / 2,
                        rect.width, rect.height, 7, 7);
                gr.setColor(Color.BLACK);
                gr.drawString(StringLabeller.getLabeller(
                        (Graph) v.getGraph()).getLabel(v),
                        x - rect.width / 2 + INSET, y + INSET);
            } finally {
                graphics.setFont(font);
            }
        }

        /**
         * @see edu.uci.ics.jung.visualization.HasShapeFunctions#
         *      getVertexShapeFunction()
         */
        public VertexShapeFunction getVertexShapeFunction() {
            return vertexShapeFunction;
        }

        /**
         * @see edu.uci.ics.jung.visualization.HasShapeFunctions#
         *      getEdgeShapeFunction()
         */
        public EdgeShapeFunction getEdgeShapeFunction() {
            return edgeShapeFunction;
        }
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
