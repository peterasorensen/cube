package cube;

import java.util.Observable;

import static java.lang.System.arraycopy;

/** Models an instance of the Cube puzzle: a cube with color on some sides
 *  sitting on a cell of a square grid, some of whose cells are colored.
 *  Any object may register to observe this model, using the (inherited)
 *  addObserver method.  The model notifies observers whenever it is modified.
 *  @author P. N. Hilfinger
 */
class CubeModel extends Observable {

    /** A blank cube puzzle of size 4. */
    CubeModel() {
        initialize(_iSide, _iRow0, _iCol0, _iPainted, _iFace);
    }

    /** A copy of CUBE. */
    CubeModel(CubeModel cube) {
        initialize(cube);
    }

    /** Initialize puzzle of size SIDExSIDE with the cube initially at
     *  ROW0 and COL0, with square r, c painted iff PAINTED[r][c], and
     *  with face k painted iff FACEPAINTED[k] (see isPaintedFace).
     *  Assumes that
     *    * SIDE > 2.
     *    * PAINTED is SIDExSIDE.
     *    * 0 <= ROW0, COL0 < SIDE.
     *    * FACEPAINTED has length 6.
     */
    void initialize(int side, int row0, int col0, boolean[][] painted,
                    boolean[] facePainted) {
        _iSide = side;
        _iRow0 = row0;
        _iCol0 = col0;
        _iPainted = painted;
        _iFace = facePainted;

        setChanged();
        notifyObservers();
    }

    /** Initialize puzzle of size SIDExSIDE with the cube initially at
     *  ROW0 and COL0, with square r, c painted iff PAINTED[r][c].
     *  The cube is initially blank.
     *  Assumes that
     *    * SIDE > 2.
     *    * PAINTED is SIDExSIDE.
     *    * 0 <= ROW0, COL0 < SIDE.
     */
    void initialize(int side, int row0, int col0, boolean[][] painted) {
        initialize(side, row0, col0, painted, new boolean[6]);
    }

    /** Initialize puzzle to be a copy of CUBE. */
    void initialize(CubeModel cube) {
        arraycopy(cube._iFace, 0, this._iFace, 0, 6);
        this._iSide = cube._iSide;
        this._iPainted = cube._iPainted;
        this._iCol0 = cube._iCol0;
        this._iRow0 = cube._iRow0;
        this._iMoves = cube._iMoves;
        setChanged();
        notifyObservers();
    }

    /** Move the cube to (ROW, COL), if that position is on the board and
     *  vertically or horizontally adjacent to the current cube position.
     *  Transfers colors as specified by the rules.
     *  Throws IllegalArgumentException if preconditions are not met.
     */
    void move(int row, int col) {
        if (row > _iSide - 1 || row < 0 || col > _iSide - 1 || col < 0) {
            throw new IllegalArgumentException();
        } else if (row - _iRow0 == 1 && col == _iCol0) {
            _iFace = new boolean[]{_iFace[4],
                    _iFace[5], _iFace[2], _iFace[3], _iFace[1], _iFace[0]};
            _iRow0 = row;
        } else if (row - _iRow0 == -1 && col == _iCol0) {
            _iFace = new boolean[]{_iFace[5],
                    _iFace[4], _iFace[2], _iFace[3], _iFace[0], _iFace[1]};
            _iRow0 = row;
        } else if (col - _iCol0 == 1 && row == _iRow0) {
            _iFace = new boolean[]{_iFace[0],
                    _iFace[1], _iFace[4], _iFace[5], _iFace[3], _iFace[2]};
            _iCol0 = col;
        } else if (col - _iCol0 == -1 && row == _iRow0) {
            _iFace = new boolean[]{_iFace[0],
                    _iFace[1], _iFace[5], _iFace[4], _iFace[2], _iFace[3]};
            _iCol0 = col;
        } else {
            throw new IllegalArgumentException();
        }
        if (_iFace[4] != _iPainted[_iRow0][_iCol0]) {
            _iFace[4] = _iPainted[_iRow0][_iCol0];
            _iPainted[_iRow0][_iCol0] = !_iFace[4];
        }
        _iMoves++;
        setChanged();
        notifyObservers();
    }

    /** Return the number of squares on a side. */
    int side() {
        return _iSide;
    }

    /** Return true iff square ROW, COL is painted.
     *  Requires 0 <= ROW, COL < board size. */
    boolean isPaintedSquare(int row, int col) {
        return _iPainted[row][col];
    }

    /** Return current row of cube. */
    int cubeRow() {
        return _iRow0;
    }

    /** Return current column of cube. */
    int cubeCol() {
        return _iCol0;
    }

    /** Return the number of moves made on current puzzle. */
    int moves() {
        return _iMoves;
    }

    /** Return true iff face #FACE, 0 <= FACE < 6, of the cube is painted.
     *  Faces are numbered as follows:
     *    0: Vertical in the direction of row 0 (nearest row to player).
     *    1: Vertical in the direction of last row.
     *    2: Vertical in the direction of column 0 (left column).
     *    3: Vertical in the direction of last column.
     *    4: Bottom face.
     *    5: Top face.
     */
    boolean isPaintedFace(int face) {
        return _iFace[face];
    }

    /** Return true iff all faces are painted. */
    boolean allFacesPainted() {
        for (int j = 0; j < _iFace.length; j++) {
            if (!(_iFace[j])) {
                return false;
            }
        }
        return true;
    }

    /** Default board size. */
    private int _iSide = 4;
    /** Default start row. */
    private int _iRow0 = 0;
    /** Default start column. */
    private int _iCol0 = 0;
    /** Default SIDE*SIDE game board with all sqaures set to FALSE. */
    private boolean[][] _iPainted = new boolean[_iSide][_iSide];
    /** Default die with all faces set to FALSE. */
    private boolean[] _iFace = new boolean[6];
    /** Default number of completed moves (0 to start game). */
    private int _iMoves = 0;

}
