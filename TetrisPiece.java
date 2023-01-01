package model;

import java.io.Serializable;
import java.util.*;

/** An immutable representation of a tetris piece in a particular rotation.
 *  Each piece is defined by the blocks that make up its body.
 *
 * Based on the Tetris assignment in the Nifty Assignments Database, authored by Nick Parlante
 */
public class TetrisPiece implements Serializable {

    /*
     Implementation notes:
     -The starter code does a few simple things
     -It stores the piece body as a TetrisPoint[] array
     -The attributes in the TetrisPoint class are x and y coordinates
     -Don't assume there are 4 points in the piece body; there might be less or more!
    */
    private TetrisPoint[] body; // y and x values that make up the body of the piece.
    private int[] lowestYVals; //The lowestYVals array contains the lowest y value for each x in the body.
    private int width;
    private int height;
    private TetrisPiece next; // We'll use this to link each piece to its "next" rotation.
    static private TetrisPiece[] pieces;	// array of rotations for this piece


    // String constants for the standard 7 tetris pieces
    public static final String STICK_STR	= "0 0	0 1	 0 2  0 3";
    public static final String L1_STR		= "0 0	0 1	 0 2  1 0";
    public static final String L2_STR		= "0 0	1 0 1 1	 1 2";
    public static final String S1_STR		= "0 0	1 0	 1 1  2 1";
    public static final String S2_STR		= "0 1	1 1  1 0  2 0";
    public static final String SQUARE_STR	= "0 0  0 1  1 0  1 1";
    public static final String PYRAMID_STR	= "0 0  1 0  1 1  2 0";

    /**
     * Defines a new piece given a TetrisPoint[] array that makes up its body.
     * Makes a copy of the input array and the TetrisPoint inside it.
     * Note that this constructor should define the piece's "lowestYVals". For each x value
     * in the body of a piece, the lowestYVals will contain the lowest y value for that x in the body.
     * This will become useful when computing where the piece will land on the board!!
     */
    public TetrisPiece(TetrisPoint[] points) {
        this.body = points; //copy of input array
        //this.pieces = new TetrisPiece[];
        //this.next = this.computeNextRotation();
        //this.next = next rotation
        //this.piece


        // find width
        ArrayList<Integer> a = new ArrayList<Integer>();
        for (TetrisPoint v : points) {
            a.add(v.x);
        }
        int min_x = Collections.min(a);
        int max_x = Collections.max(a);
        this.width = max_x - min_x + 1;

        // find height
        ArrayList<Integer> b = new ArrayList<Integer>();
        for (TetrisPoint v : points) {
            b.add(v.y);
        }
        int min_y = Collections.min(b);
        int max_y = Collections.max(b);
        this.height = max_y - min_y + 1;


        //Lowest Y Val
        TreeMap<Integer, ArrayList<Integer>> stored = new TreeMap<Integer, ArrayList<Integer>>(); // TreeMap

        for (TetrisPoint v : points) {
            if (stored.containsKey(v.x)) {
                stored.get(v.x).add(v.y);
            } else {
                ArrayList<Integer> later = new ArrayList<Integer>();
                later.add(v.y);
                stored.put(v.x, later);
            }
        }
        //System.out.println("Step 1:" + stored);

        ArrayList<Integer> smallest = new ArrayList<>();
        for (Integer v : stored.keySet()) {
            ArrayList<Integer> ne = new ArrayList<Integer>();
            ne = stored.get(v);
            int small = Collections.min(ne);
            smallest.add(small);

        }
        //System.out.println("Smallest:" + smallest);
        //System.out.println("Width:" + this.width);
        //ystem.out.println("Height:" + this.height);

        this.lowestYVals = new int[this.width];
        for (int i=0; i < this.lowestYVals.length; i++) {
            this.lowestYVals[i] = smallest.get(i).intValue();
        }

    }




    /**
     * Alternate constructor for a piece, takes a String with the x,y body points
     * all separated by spaces, such as "0 0  1 0  2 0  1 1".
     */
    public TetrisPiece(String points) {
        this(parsePoints(points));
    }

    /**
     * Returns the width of the piece measured in blocks.
     *
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the piece measured in blocks.
     *
     * @return height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns a pointer to the piece's body. The caller
     * should not modify this array.
     *
     * @return point array that defines piece body
     */
    public TetrisPoint[] getBody() {
        return body;
    }

    /**
     * Returns a pointer to the piece's lowest Y values. For each x value
     * across the piece, this gives the lowest y value in the body.
     * This is useful for computing where the piece will land (if dropped).
     * The caller should not modify the values that are returned
     *
     * @return array of integers that define the lowest Y value for every X value of the piece.
     */
    public int[] getLowestYVals() {
        return lowestYVals;
    }

    /**
     * Returns true if two pieces are the same --
     * their bodies contain the same points.
     * Interestingly, this is not the same as having exactly the
     * same body arrays, since the points may not be
     * in the same order in the bodies. Used internally to detect
     * if two rotations are effectively the same.
     *
     * @param obj the object to compare to this
     *
     * @return true if objects are the same
     */
    public boolean equals(Object obj) {
        if (obj instanceof TetrisPiece) {
            TreeMap<Integer, ArrayList<Integer>> stored = new TreeMap<Integer, ArrayList<Integer>>(); // TreeMap

            for (TetrisPoint v : ((TetrisPiece) obj).body) {
                if (stored.containsKey(v.x)) {
                    stored.get(v.x).add(v.y);
                    Collections.sort(stored.get(v.x));
                } else {
                    ArrayList<Integer> later = new ArrayList<Integer>();
                    later.add(v.y);
                    stored.put(v.x, later);
                    Collections.sort(stored.get(v.x));
                }
            }
            //System.out.println(stored);


            TreeMap<Integer, ArrayList<Integer>> stored_two = new TreeMap<Integer, ArrayList<Integer>>(); // TreeMap

            for (TetrisPoint v : this.getBody()) {
                if (stored_two.containsKey(v.x)) {
                    stored_two.get(v.x).add(v.y);
                    Collections.sort(stored_two.get(v.x));
                } else {
                    ArrayList<Integer> lat = new ArrayList<Integer>();
                    lat.add(v.y);
                    stored_two.put(v.x, lat);
                    Collections.sort(stored_two.get(v.x));
                }
            }
            //System.out.println(stored_two);
            return stored.equals(stored_two);
        } else {
            return false;
        }


    }

    /**
     * This is a static method that will return all rotations of
     * each of the 7 standard tetris pieces:
     * STICK, L1, L2, S1, S2, SQUARE, PYRAMID.
     * The next (counterclockwise) rotation can be obtained
     * from each piece with the fastRotation() method.
     * This method will be called by the model to facilitate
     * selection of random pieces to add to the board.
     * The pieces can be easily rotated because the rotations
     * have been precomputed.
     *
     * @return a list of all the rotations for all the given pieces.
     */
    public static TetrisPiece[] getPieces() {
        // lazy evaluation -- create static array only if needed
        if (TetrisPiece.pieces==null) {
            // use makeFastRotations() to compute all the rotations for each piece
            try {
                TetrisPiece.pieces = new TetrisPiece[]{
                        makeFastRotations(new TetrisPiece(STICK_STR)),
                        makeFastRotations(new TetrisPiece(L1_STR)),
                        makeFastRotations(new TetrisPiece(L2_STR)),
                        makeFastRotations(new TetrisPiece(S1_STR)),
                        makeFastRotations(new TetrisPiece(S2_STR)),
                        makeFastRotations(new TetrisPiece(SQUARE_STR)),
                        makeFastRotations(new TetrisPiece(PYRAMID_STR)),
                };
            } catch (UnsupportedOperationException e) {
                System.out.println("You must implement makeFastRotations!");
                System.exit(1);
            }
        }
        return TetrisPiece.pieces;
    }

    /**
     * Returns a pre-computed piece that is 90 degrees counter-clockwise
     * rotated from the receiver. Fast because the piece is pre-computed.
     * This only works on pieces set up by makeFastRotations(), and otherwise
     * just returns null.
     *
     * @return the next rotation of the given piece
     */
    public TetrisPiece fastRotation() {
        return next;
    }

    /**
     * Given the "first" root rotation of a piece, computes all
     * the other rotations and links them all together
     * in a circular list. The list should loop back to the root as soon
     * as possible.
     * Return the root piece. makeFastRotations() will need to relies on the
     * pointer structure setup here. The suggested implementation strategy
     * is to use the subroutine computeNextRotation() to generate 90 degree rotations.
     * Use this to generate a sequence of rotations and link them together.
     * Continue generating rotations until you generate the piece you started with.
     * Use Piece.equals() to detect when the rotations
     * have gotten you back to the first piece.
     *
     * @param root the default rotation for a piece
     *
     * @return a piece that is a linked list containing all rotations for the piece
     */
    public static TetrisPiece makeFastRotations(TetrisPiece root) {
        // Create Linked List
        // compute next rotations, link in list, keep going until piece equals the first one again
        // turn them into a linked list
        // take root
        //make root.next the next rotation
        //
        TetrisPiece pre = root;
        TetrisPiece curr = root.computeNextRotation();
        root.next = curr;
        while (!root.equals(curr)) {
            pre = curr;
            curr.next = curr.computeNextRotation();
            curr = curr.next;
        }
        pre.next = root;
        return root;
    }


    /**
     * Returns a new piece that is 90 degrees counter-clockwise
     * rotated from the receiver.
     *
     * @return the next rotation of the given piece
     */
    public TetrisPiece computeNextRotation() {

        if (this.getWidth() == 2 && this.getHeight() == 2) {
            TetrisPiece n = new TetrisPiece("0 0  0 1  1 0  1 1");
            return n;
        } else if ((this.getWidth() == 4 && this.getHeight() == 1) || (this.getWidth() == 1 && this.getHeight() == 4)) {
            if (this.getWidth() == 4 && this.getHeight() == 1) {
                TetrisPiece n = new TetrisPiece("0 0  0 1  0 2  0 3");
                return n;
            } else {
                TetrisPiece n = new TetrisPiece("0 0  1 0  2 0  3 0");
                return n;
            }
        } else {
            ArrayList<TetrisPoint> arr = new ArrayList<TetrisPoint>();
            for (TetrisPoint v : this.getBody()){
                TetrisPoint first = new TetrisPoint(v.y, -v.x);
                arr.add(first);
            }

            TetrisPoint[] lowestval = new TetrisPoint[arr.size()];
            for (int i=0; i < lowestval.length; i++) {
                lowestval[i] = arr.get(i);
            }

            TetrisPiece rot = new TetrisPiece(lowestval);
            int[] y = rot.getLowestYVals();
            Arrays.sort(y);
            int new_y = y[0];

            ArrayList<TetrisPoint> arr_two = new ArrayList<TetrisPoint>();
            for (TetrisPoint v : rot.getBody()){
                TetrisPoint second = new TetrisPoint(v.x, v.y + (-1 * new_y));
                arr_two.add(second);
            }

            TetrisPoint[] fin = new TetrisPoint[arr_two.size()];
            for (int i=0; i < fin.length; i++) {
                fin[i] = arr_two.get(i);
            }

            TetrisPiece fin_rotation = new TetrisPiece(fin);
            return fin_rotation;

        }
        //return


        // /stick (compare w, h) then :


        // else use formula
        // iterate through each point :
        // for each point, create new point, (y,-x) add it to array list
        //turn array list to array
        //find lowest return value by making it a piece
        //iterate through each point
        //for each point, add -lyval to the y component
        //turn array list to array
        //return new piece with point update

    }

    /**
     * Print the points within the piece
     *
     * @return a string representation of the piece
     */
    public String toString() {
        String str = "";
        for (int i = 0; i < body.length; i++) {
            str += body[i].toString();
        }
        return str;
    }

    /**
     * Given a string of x,y pairs (e.g. "0 0 0 1 0 2 1 0"), parses
     * the points into a TPoint[] array.
     *
     * @param string input of x,y pairs
     *
     * @return an array of parsed points
     */
    private static TetrisPoint[] parsePoints(String string) {
        List<TetrisPoint> points = new ArrayList<TetrisPoint>();
        StringTokenizer tok = new StringTokenizer(string);
        try {
            while(tok.hasMoreTokens()) {
                int x = Integer.parseInt(tok.nextToken());
                int y = Integer.parseInt(tok.nextToken());

                points.add(new TetrisPoint(x, y));
            }
        }
        catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse x,y string:" + string);
        }
        // Make an array out of the collection
        TetrisPoint[] array = points.toArray(new TetrisPoint[0]);
        return array;
    }
}
