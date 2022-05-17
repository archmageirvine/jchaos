package chaos.board;

import java.io.Serializable;

import chaos.common.Actor;
import chaos.common.State;
import chaos.common.inanimate.MagicGlass;

/**
 * Implements a line-of-sight strategy based on 8&times;8 masks. This
 * implementation will work on screens up to 65536&times;65536 pixels.
 *
 * <pre>
 *  Line of Sight Summary
 *  ---------------------
 *
 *  Sean A. Irvine, 2003-11-19
 *
 *  Conceptually the line-of-sight problem is trivial.  An actor in one
 *  cell, hereafter called the "source" is trying to see another actor in
 *  a (usually different) cell, hereafter called the "target".  Whether or
 *  not the target can be seen depends solely on what obstacles appear on
 *  the geodesic between the source and target.  In Chaos the geodesic is
 *  always a straight line between the source and target.
 *
 *  Line-of-sight determinations are made during ranged combat (unless the
 *  source has archery ability) and in determining the legality of
 *  numerous spell casts.  It needs to be done quickly because the
 *  computer players will attempt many such determinations as they
 *  evaluate different strategic options.
 *
 *  Let isLOS(source,target) be the function implementing a line-of-sight
 *  test from "source" to "target".  It returns true if "source" can see
 *  "target" and false otherwise.  It obeys the rules:
 *
 *  isLOS(a,a) = true
 *  isLOS(a,b) = true if and only if isLOS(b,a) = true
 *
 *  The first rule can be stated "you can always see yourself". The second
 *  rule can be stated "the set of cells 'a' can see is the same as the
 *  set of cells which can see 'a'".
 *
 *  These rules make useful test cases.  I found two subtle bugs by
 *  testing the second rule.
 *
 *  For the mathematicians: isLOS() is not an equivalence relation because
 *  it does not obey the triangle inequality.  That is, suppose isLOS(a,b)
 *  = true and isLOS(b,c); then it is not necessarily the case that
 *  isLOS(a,c) is true.
 *
 *  In essence the line-of-sight problem is the same as the line drawing
 *  problem.  Fortunately, there are many known ways to quickly draw
 *  lines, although this is often performed directly in hardware on modern
 *  graphics cards.  These methods have names like the digital difference
 *  analyzer (DDA), Bresenham's algorithm, and Wu's algorithm.  Each has
 *  its strengths and weaknesses, depending on the style of line, the
 *  target CPU or hardware, and other display considerations.  Older
 *  versions of Chaos use a variation on Bresenham's algorithm.
 *
 *  Line drawing algorithms are basically an extremely efficient
 *  implementation of the loop:
 *
 *  for (point on line) {
 *    plot(point)
 *  }
 *
 *  For line-of-sight what we want is:
 *
 *  for (point on line) {
 *    if (getPixel(point) != BLACK) {
 *      return false;
 *    }
 *  }
 *  return true;
 *
 *  Roughly speaking, line-of-sight fails whenever there is a non-black
 *  pixel on the line from the source to the target.  In reality, there
 *  are additional complications.  For example, we don't want to reject
 *  line-of-sight because of pixels within the source or target cell.
 *  Likewise, we need to be able to ignore background terrain.  Finally,
 *  certain objects don't block line-of-sight even though they have
 *  non-black pixels.  These include low lying objects like pits and
 *  corpses and insubstantial objects like fire and certain undeads.
 *  Finally, we assume that line-of-sight testing is fast enough that we
 *  can ignore animation during the test (or more realistically we can
 *  halt animation for the duration of the test without noticeable on
 *  screen effect).
 *
 *  The way Chaos copes with cells is by having methods like accept(cell)
 *  and reject(cell).  If accept(cell) is true, this indicates that the
 *  given cell never blocks line of sight.  The less useful reject(cell)
 *  method returns true if the cell always blocks line-of-sight, as might
 *  be the case for walls.  Both methods can give important speed boosts
 *  to line-of-sight tests.
 *
 *  Thus, expanding our pseudocode:
 *
 *  for (point on line) {
 *    cell = getCell(point);
 *    if (reject(cell)) {
 *      return false;
 *    }
 *    if (!accept(cell) &amp;&amp; getPixel(point) != BLACK) {
 *      return false;
 *    }
 *  }
 *  return true;
 *
 *  A deficiency with this code is that accept() and reject() called many
 *  times for each cell.  Later we will see how to make sure they are only
 *  called once per cell.
 *
 *  Blizzard
 *  --------
 *
 *  This section introduces my new line drawing algorithm I'm calling
 *  Blizzard.  "Blizzard" happened to be the first word that came to mind
 *  when I was trying to think of a name.
 *
 *  Blizzard is an 8-way stepping algorithm using fixed-point arithmetic.
 *  It draws lines without using any branching instructions in the central
 *  loop (a highly desirable feature on modern CPUs).  It does require
 *  shift instructions and therefore might not be the best choice for
 *  older CPUs without barrel shifters.  It is quadrant based; thereby
 *  halving the code length compared to Bresenham.  Actually, it can be
 *  implemented in only two cases, but I'll explain only the quadrant
 *  version here.
 *
 *  For every line, the distance moved in one axis is greater than or
 *  equal to the distance in the other axis.  Assume it is the x axis
 *  (the y case is completely symmetrical). Then the line can be made by
 *  looping over the desired x values stepping either one pixel to the
 *  left or right at each stage.  For each change in x the y value changes
 *  by some proper fraction of the x value.
 *
 *  Let (sx,sy) denote the start point and (tx,ty) the target point.  As
 *  the algorithm progresses, (sx,sy) is updated until it is the same as
 *  (tx,ty).
 *
 *  The Bresenham algorithm uses an error term, e, to detect when the y
 *  value should increase or decrease.  The following code is the
 *  Bresenham octant where tx &gt;= sx and ty &gt;= sy:
 *
 *  final int twodelx = 2 * (tx - sx);
 *  final int twodely = 2 * (ty - sy);
 *  int e = 0;
 *  while (sx != tx) {
 *    plot(sx, sy);
 *    if (e &gt; 0) {
 *      sy++;
 *      e -= twodelx;
 *    }
 *    sx++;
 *    e += twodely;
 *  }
 *
 *  Blizzard replaces the error term with a fractional y coordinate.  To
 *  accurately represent this fraction, a fixed-point arithmetic is used
 *  for y: 16 bits for the integer part and 16 bits for the fractional
 *  part.  At any time (sx,sy &gt;&gt;&gt; 16) will correspond to a valid pixel.
 *  By doing this conditionals are avoided in the central loop.  Notes for
 *  C programmers: &gt;&gt;&gt; is right shift with zero fill, it is equivalent to
 *  &gt;&gt; applied to unsigned variables. "final" is a Java keyword that means
 *  that the variable's value cannot be modified; it can be deleted in C
 *  programs.  The initial point has 0.5 added to it, to make sure we
 *  start in the centre of the pixel.
 *
 *  Here is the Blizzard algorithm for tx &gt;= sx and dx &gt;= dy:
 *
 *  final int dx = tx - sx;
 *  final int dy = ty - sy;
 *  final int slope = (dy &lt;&lt; 16) / dx;
 *  sy = (sy &lt;&lt; 16) + HALF;
 *  while (sx &lt;= tx) {
 *    plot (sx, sy &gt;&gt;&gt; 16);
 *    sy += slope;
 *    sx++;
 *  }
 *
 *  The thing to note here is the central loop is significantly simplified
 *  at the expense of a single division outside the loop.  For longer
 *  lines this represents a considerable speed improvement.  The value of
 *  HALF is 32768, which is 0.5 in the fixed-point arithmetic.  The
 *  variable "slope" essentially holds how much the y-coordinate should
 *  change for each increment of the x-coordinate.
 *
 *  Here is the full algorithm of Blizzard for all quadrants.  It draws a
 *  line from (sx,sy) to (tx,ty).
 *
 *  void line(final int sx, final int sy, final int tx, final int ty) {
 *    final int dx = tx - sx;
 *    final int dy = ty - sy;
 *    if (abs(dx) &gt;= abs(dy)) {
 *      // x-axis movement is the longer
 *      final int slope = dx == 0 ? 0 : (dy &lt;&lt; 16) / dx;
 *      sy = (sy &lt;&lt; 16) + HALF;
 *      if (dx &gt;= 0) {
 *        while (sx &lt;= tx) {
 *          plot(sx, sy &gt;&gt;&gt; 16);
 *          sy += slope;
 *          sx++;
 *        }
 *      } else {
 *        while (sx &gt;= tx) {
 *          plot(sx, sy &gt;&gt;&gt; 16);
 *          sy -= slope;
 *          sx--;
 *        }
 *      }
 *    } else {
 *      // y-axis movement is the longer
 *      final int slope = (dx &lt;&lt; 16) / dy;
 *      sx = (sx &lt;&lt; 16) + HALF;
 *      if (dy &gt;= 0) {
 *        while (sy &lt;= ty) {
 *          plot(sx &gt;&gt;&gt; 16, sy);
 *          sx += slope;
 *          sy++;
 *        }
 *      } else {
 *        while (sy &gt;= ty) {
 *          plot(sx &gt;&gt;&gt; 16, sy);
 *          sx -= slope;
 *          sy--;
 *        }
 *      }
 *    }
 *  }
 *
 *  In many circumstances it is worth special casing horizontal (dy=0) and
 *  vertical (dx=0) lines.  In Chaos such lines are more common than
 *  average because the cell restriction means that we draw lines at fewer
 *  than the total number of possible angles.  It is easy to modify
 *  Blizzard to handle these cases.  There is an example of how to do this
 *  in the jChaos line-of-sight implementation.  Based on a random
 *  selection of 10 million lines making special cases here gives a 4%
 *  speed boost on a 25x25 cell board.
 *
 *  LOS Optimizations
 *  -----------------
 *
 *  The presence of both a cell level and pixel level mean there is a
 *  number of optimizations which can performed.  Most of these arise
 *  because we can often tell immediately from the contents of a cell
 *  whether or not line-of-sight is blocked.
 *
 *  Let w be the width of a cell in pixels.  Typical values for w are 8,
 *  16, 32, 64. Further assume both pixels and cells are numbered from 0
 *  at the top left, then the cell containing the pixel (sx,sy) is given
 *  by
 *
 *  cell = (sy / w) * CellsPerRow + (sx / w).
 *
 *  When w is a power of two (as is usually the case), w = 2^q, then
 *  the divisions should be replaced with shifts.
 *
 *  cell = (sy &gt;&gt; q) * CellsPerRow + (sx &gt;&gt; q).
 *
 *  Because we will be doing this, we can modify the stopping condition
 *  from sx == tx to cell == target.  This means the algorithm stops as
 *  soon as we enter the target cell, saving a few pixel tests.
 *
 *  When performing a line-of-sight test at the pixel level we need to
 *  choose which pixel of the source cell to start from and which pixel of
 *  the target cell to end at.  Obviously we should choose the pixel in
 *  the middle.  The problem is that there are four central pixels!  Old
 *  versions of Chaos used to try each in turn.  This was computationally
 *  expensive, so use the centre pixel closest to the other cell at each
 *  end, giving a four-fold increase in performance.  This does mean that a
 *  few sightings which would have been previously possible are no longer
 *  possible, but the number of these cases is so small it is unlikely any
 *  player would notice.  In the case of a horizontal shot there remains a
 *  choice of two pixels, we arbitrarily select the upper of the two choices.
 *  Likewise for a vertical shot the left of the two choices is used.
 *
 *  The following (ugly) picture shows a source in the lower left trying
 *  to see a target four cells to the right and one cell above.  The
 *  double lines are cell boundaries, each cell is 8x8 pixels.  *'s
 *  represent obstructing pixels.  The O's are the pixels examined on the
 *  line from the closest centre pixels.  In this case there is no
 *  line-of-sight because of the head of the monster in the cell adjacent
 *  to the source.  The X's represent a different path using different
 *  centre cells.  In this case line-of-sight would have succeeded.  Note
 *  where the X and O paths overlap, I've shown only the O path.
 *
 *  #===============#===============#===============#===============#
 *  " | | | | | | | " | | | | | | | " | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | | | | | | | " | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | | | | | | | " | | | | | | | " |  TARGET   | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | | | | | | | " | | | | | | | " | |X|X| | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | | | | | | | " | | | | | | |X"X|X|O|O| | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | | | | | | | " | | | |X|X|X|O"O|O| | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | | | | | | | " |X|X|O|O|O|O| " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | | | | | |X|X"O|O|O| | | | | " | | | | | | | "
 *  #===============#===============#===============#===============#
 *  " | | | | | | | " | | |X|X|O|O|O" | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | "X|O|O|*|*| | | " | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | |X|O|O"O| | |*|*| | | " | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | |X|O|O| | " | |*|*|*|*| | " | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | |*|*|*|*| | " | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " |  SOURCE   | " | |*| |*| | | " | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " |*|*| |*|*| | " | | | | | | | " | | | | | | | "
 *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *  " | | | | | | | " | | | | | | | " | | | | | | | " | | | | | | | "
 *  #===============#===============#===============#+===============#
 *
 *  Nevertheless, using only the closest centre cells is a very sensible
 *  optimization.  The following code selects the appropriate pixel within
 *  the cell at each end given that (osx,osy) it the top left of the
 *  source cell and (tx,ty) is the top left of the target cell:
 *
 *  // adjust (sx,sy) to centre pixel of source closest to (tx,ty)
 *  int sx = osx + (osx &gt;= tx ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH);
 *  int sy = osy + (osy &gt;= ty ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH);
 *  // adjust (tx,ty) to centre pixel of target closest to (sx,sy)
 *  tx += osx &lt;= tx ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH;
 *  ty += osy &lt;= ty ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH;
 *
 *  Having gone to all this trouble to work out the correct starting point
 *  from the line, we now immediately want to step to the next cell
 *  because nothing in the source cell should block line-of-sight.  In the
 *  Bresenham code this was accompished by making w/2 iterations in a
 *  loop.  With Blizzard we can step immediately to the correct position
 *  in the next cell (for tx &gt;= sx and dx &gt;= dy):
 *
 *  sx += w / 2;
 *  sy += (w / 2) * slope;
 *
 *  In practice, because w is known at compile time, the value of w/2 is
 *  known and not computed at runtime, hence these divisions do not occur
 *  in the actual implementation.
 *
 *  Before getting to the problem of stepping over cells, it is worth
 *  working out a few more formulas for various pixels within a cell.  Let
 *  w=2^q be the width of a cell in pixels. Let v = w - 1.  Note because w
 *  is a power of two v has the binary expansion 11...1 (q ones).  Suppose
 *  X is any pixel within a cell with coordinates (sx,sy) then the
 *  following picture shows a number of useful points of the cell in terms
 *  of (sx,sy).
 *
 *          &lt;- m -&gt;
 *         #===============#
 *       ^ "A| | | | | | |B"      A = (sx &amp; ~v, sy &amp; ~v)
 *       | +-+-+-+-+-+-+-+-+      B = ((sx &amp; ~v) + v, sy &amp; ~v)
 *       n " | | | | | | | "      C = (sx &amp; ~v, (sy &amp; ~v) + v)
 *       | +-+-+-+-+-+-+-+-+      D = ((sx &amp; ~v) + v, (sy &amp; ~v) + v)
 *       V " | | |X| | | | "      m = sx - (sx &amp; ~v)
 *         +-+-+-+-+-+-+-+-+      n = sy - (sy &amp; ~v)
 *         " | | | | | | | "
 *         +-+-+-+-+-+-+-+-+
 *         " | | | | | | | "
 *         +-+-+-+-+-+-+-+-+
 *         " | | | | | | | "
 *         +-+-+-+-+-+-+-+-+
 *         " | | | | | | | "
 *         +-+-+-+-+-+-+-+-+
 *         "C| | | | | | |D"
 *         #===============#
 *
 *  Blizzard can be used as described with plot() replaced by checkPixel()
 *  as a line-of-sight algorithm.  However, we can make it better by
 *  supporting accept() and reject() at a cell level.  Ideally we only
 *  want to call accept() and reject() once per cell because these
 *  functions might involve quite a lot of work.  We can do this provided
 *  we can quickly tell when a change in (sx,sy) is a change in cell.
 *
 *  Given two points (ax,ay) and (bx,by) one way to test if they are the
 *  same cell is
 *
 *  getCell(ax,ay) == getCell(bx,by)
 *
 *  but this has the disadvantage of two functions calls each involving
 *  several arithmetic operations.  I claim a faster way is the
 *  obscure looking
 *
 *  ((ax ^ bx) | (ay ^ by)) &lt; w
 *
 *  This may not be immediately obvious.  Skip the rest of this paragraph
 *  if you don't care why it is true.  Consider the expression ax ^ bx.
 *  This gives precisely those bits which differ between ax and bx.
 *  Thus, if (ax ^ bx) &lt; w, this means the difference in value is less
 *  than the width of a cell.  Because cells are perfectly aligned at a
 *  power of 2, this also suffices to test if ax and bx are in the same
 *  column of cells.  Similarly, (ay ^ by) &lt; w tests if we are in the same
 *  row of cells. Thus, ((ax ^ bx) &lt; w) &amp;&amp; ((ay ^ by) &lt; w) tests we are
 *  in the same row and the same column (i.e. the same cell).  This
 *  simplifies to ((ax ^ bx) | (ay ^ by)) &lt; w.
 *
 *  Suppose then we have just entered a new cell.  Compute the cell number
 *  from (sx,sy).  If reject(cell) is true then we are done, there is no
 *  line of sight.  If accept(cell) is true then we want to step
 *  immediately to the next cell.  It would be nice to do this in one step
 *  and I thought at one point I had nailed this problem, by always
 *  stepping w pixels, but it turned out to be a brain crash on my part.
 *  One way I know which does work is to use the condition above in a
 *  body-less loop.  For simplicity of discussion consider the case where
 *  tx &gt;= sx and dx &gt;= dy, then the loop is
 *
 *  while (((csx ^ ++sx) | (csy ^ (sy += slope))) &lt; w);
 *
 *  Here (csx,csy) is the pixel at which we first entered this cell.
 *
 *  This issue deserves more thought.  It would be nice to know in advance
 *  exactly how many iterations are needed to get to the next cell; because
 *  if we knew in advance how many iterations, say i, then we could
 *  immediately step to the next cell with:
 *
 *  sx += i;
 *  sy += i * slope;
 *
 *  thereby obviating the need for the while loop.
 *
 *  My original thought here was to take i = w (the cell width).  This
 *  does indeed always step to another cell, but unfortunately it can step
 *  over an entire cell.  What we need is a value of i which takes us to
 *  the first pixel in the next cell to be tested.
 *
 *  Here is an example where taking i = w does work:
 *
 *        #===============#===============#
 *        " | | | | | | | " | | | |O|O| | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | " | |O|O| | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | "X|O| | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | |O|O" | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | |O|O| | " | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | |O|O| | | | " | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        "C|O| | | | | | " | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | " | | | | | | | "
 *        #===============#===============#
 *
 *  Suppose the pixel marked C = (sx,sy) is the current pixel and we want to
 *  automatically accept this cell.  Then doing sx += 8 and sy += 8*slope
 *  takes us to X the first pixel in the next cell.  Unfortunately, if C
 *  had initially been higher in the first cell then this procedure would
 *  not have worked:
 *
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | " | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | " | | | | | |Z|O"
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | " | | | |O|O| | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | " | |O|O| | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | "X|O| | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | |Y|O" | | | | | | | "
 *        +===============#===============+  &lt;- cell boundary
 *        " | | | |O|O| | " | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | |O|O| | | | " | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        "C|O| | | | | | " | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *        " | | | | | | | " | | | | | | | "
 *        +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 *                        ^___cell boundary

 *  Here we have indeed ended up at the first pixel in a cell to be tested
 *  but we have inadvertently omitted to check anything in the top left
 *  cell through which the line clearly passes.  We really wanted to step
 *  to Y not X. This example also reminds us that the first pixel
 *  encountered in a cell need not be the left-most edge of the cell.  If
 *  we step w cells for Y we end up at Z, also clearly wrong.
 *
 *  Staying with the case tx &gt;= sx and dx &gt;= dy the first pixel of any
 *  cell will be on the left, top, or bottom edge.  Further, excepting the
 *  top-left pixel, those on the top edge are only encountered when
 *  slope &lt; 0 and those on the bottom edge when slope &gt; 0.
 *
 *           #===============#
 *           "A|B|B|B|B|B|B|B"
 *           +-+-+-+-+-+-+-+-+
 *           "A| | | | | | | "
 *           +-+-+-+-+-+-+-+-+
 *           "A| | | | | | | "
 *           +-+-+-+-+-+-+-+-+
 *           "A| | | | | | | "
 *           +-+-+-+-+-+-+-+-+
 *           "A| | | | | | | "
 *           +-+-+-+-+-+-+-+-+
 *           "A| | | | | | | "
 *           +-+-+-+-+-+-+-+-+
 *           "A| | | | | | | "
 *           +-+-+-+-+-+-+-+-+
 *           "A|B|B|B|B|B|B|B"
 *           #===============#
 *
 *  Consider the A pixels first; i.e., those where (sx &amp; v) == 0 and suppose
 *  slope &gt; 0 then the new pixel is to the right and/or above the current
 *  pixel.  Essentially, we can compute the smallest required x-coordinate
 *  to reach a pixel with y-coordinate (sy &amp; ~v) - 1.  If this is less
 *  than w pixels from the current x position then we enter the cell above
 *  otherwise the cell to the right.   This yields the following code (not
 *  adjusted for the fixed-point arithmetic):
 *
 *  if ((sx &amp; v) == 0) {
 *    sx += 1 + (slope &lt;= 0 ? -sy : w - sy) / slope;
 *    if (sx - csx &lt; w) {
 *      // next cell is above
 *      sy += (sx - csx) * slope;
 *    } else {
 *      // next cell is to the right
 *      sy += w * slope;
 *      sx = csx + w;
 *    }
 *  }
 *
 *  Coming up with this code took a little trigonometry and much careful
 *  testing.  The important part is to make sure the determinations are
 *  made on the x-coordinate (more precisely, the coordinate not subject
 *  to fixed-point arithmetic).  Precomputing 1/slope so that the division
 *  can be replaced my a multiplication may give additional speed
 *  improvement, I've not investigated this.
 *
 *  In the case of the B pixels we can only ever enter the cell to the
 *  right and the following update suffices.
 *
 *  if ((sy &amp; v) == v) {
 *    sx = (sx + w) &amp; ~v;
 *    sy += (sx - csx) * slope;
 *  }
 *
 *  Unfortunately, the complexity of these expressions mean that they give
 *  no appreciable speedup when w is as small as 8.  It would be nice if
 *  we could squeeze a few more instructions out of these conditions.
 *
 *  Performance
 *  -----------
 *
 *  The following tables are some performance figures.  Each run is based
 *  on two hundred million random line-of-sight determinations on a 25x25
 *  world. Those for the 12.5% density are based on two billion
 *  determinations.  These tests were run on a fully loaded 600 MHz
 *  Pentium-III.
 *
 *      density   w    unoptimized  optimized
 *       (%)   (pixels)  (los/s)     (los/s)
 *      --------------------------------------
 *        0.0     8       237331      237911
 *        0.0    32       127818      226803
 *
 *       12.5     8       333600      327382
 *       12.5    16       316357      351614
 *       12.5    32       250388      333445
 *       12.5    64       153040      250666
 *
 *       25.0     8       335795      316292
 *       25.0    32       334788      385240
 *
 *       50.0     8       430373      409600
 *       50.0    32       427104      433455
 *
 *      100.0     8       481638      461392
 *      100.0    32       443398      450400
 *
 *  The "density" column refers to the number of obstructions on the
 *  board. 0% means the board was empty and 100% that every cell contained
 *  a monster.  A value of 12.5% corresponds to 1 in 8 cells having a
 *  monster.  The "w" column is the number of pixels per row and per
 *  column of a cell.  The only difference between the "unoptimized" and
 *  "optimized" runs are whether or not direct cell stepping is used.
 *  Both give the number of line-of-sight determinations which can be made
 *  in one second.
 *
 *  It can be seen that as the creature density rises line-of-sight gets
 *  quicker, this is because typically less points need to be examined
 *  before the line is rejected.  Similarly, the difference between the
 *  pixel only and cell-stepping versions decreases because there are less
 *  empty cells where the cell-stepping version has an advantage.
 *
 *  A second effect is that the cell-stepping version is actually only
 *  faster when w &gt; 8.  This is because the extra logic needed to choose
 *  how to step to the next cell is about the same as making 8 pixel
 *  steps.  Notice, however, that cell-stepping is significantly faster
 *  as w increases.
 *
 *  On a 1.6 GHz Athlon, w = 8, both versions achieve well over 900,000
 *  determinations per second.  A 2 GHz machine should easily perform
 *  million determinations per second.
 *
 *  jChaos
 *  ------
 *
 *  Line-of-sight in jChaos is implemented in <code>chaos.board.LineOfSight.</code>
 *  It uses Blizzard.  Rather than directly examining the video memory which
 *  could be different on each client, it works directly on a set of masks
 *  inferred from the tile set graphics.  This approach should be
 *  considered experimental.  Exactly how the masks are distilled from the
 *  graphics need not concern us here, suffice it to say
 *
 *  java chaos.common.DumpCreature creature-class
 *
 *  will show you the mask along with all the other information for the
 *  monster.  The masks are 8x8 pixels, 1 bit deep.  For example, the wood
 *  elf has the mask:
 *
 *     +--------+
 *     |........|
 *     |...**...|
 *     |..****..|
 *     |...**...|
 *     |...**...|
 *     |...**...|
 *     |...**...|
 *     |........|
 *     +--------+
 *
 *  Although the fidelity of these masks is low, they have the redeeming
 *  feature that they can be represented by a single 64-bit integer.
 *  Just treat the mask as binary with .=0 and *=1 in normal reading
 *  order.  Thus getLOSMask(WoodElf) = 0x00183c1818181800L.  For any cell an
 *  all zero mask means the cell should be automatically accepted and the
 *  all one mask that the cell should be automatically rejected.  In the
 *  most difficult case of a pixel test we need only check the appropriate
 *  bit of the long.  This is way quicker than consulting video memory.
 *
 *  Thus in jChaos accept() and reject() are very simple functions:
 *
 *  accept(cell) { return getLOSMask(cell) == 0L; }
 *  reject(cell) { return getLOSMask(cell) == ~0L; }
 *
 *  In the actual implementation these are inlined for extra speed.
 *
 *  The implementation has a number of test cases.  I've run about a
 *  hundred billion isLOS() tests on random arrangements so far.  No
 *  infinite loops or inconsistencies have occurred.
 *
 *  TChaos
 *  ------
 *
 *  In TChaos line-of-sight determinations are made directly against video
 *  memory.  At the time of writing it uses a mixture of hand-optimized C
 *  and assembler based on Bresenham's algorithm.  Consult cell.c in
 *  TChaos for the current status.  The accept() conditions are more
 *  complicated than for jChaos.  The cell width is w = 32.
 *
 *  Suggestions/hints for getting a Blizzard version in TChaos:
 *
 *  Make sure you understand the Blizzard line drawer first.  This will
 *  help debugging when things don't work as expected.  I'd recommend
 *  making a simple line drawing program in C first.
 *
 *  Port LineOfSight.java to C.  For TChaos, CELL_WIDTH_BITS = 5.  You
 *  can globally delete "private", "final". The variable mWidth is the
 *  width of the screen in cells (get the correct value from defines.h).
 *  Don't fiddle with any of the other constants at the top, you might
 *  have to make them #defines rather than variables.  For the first
 *  attempt delete (or comment out) the special cases for horizontal
 *  and vertical lines.  Delete the constructor and the getLOSMask()
 *  function.
 *
 *  Replace "if (mask == ~0L)" lines with "if (reject(cell))" and
 *  "if (mask == 0L)" with "if (accept(cell))".
 *
 *  Replace the mask test with a pixel test on video memory.  This should
 *  be essentially the same test as in the Bresenham code.  Blizzard
 *  assumes cells are aligned starting from (0,0) in pixel space.  Thus,
 *  when checking a pixel it may be necessary to add an offset. Maybe
 *  the LOSBitPlane is already adjusted to work this way, I'm not sure.
 *
 *  If there are problems you might need to make sx and sy unsigned
 *  (I think shifting in C on the Amiga always zero fills, so hopefully
 *  this will not be an issue.)
 *
 *  If you get a C version working, it should be comparatively easy to
 *  convert the core loops to assembler for extra speed.
 * </pre>
 *
 * @author Sean A. Irvine
 */
public class LineOfSight implements Serializable {

  /** Bits in the cell width number. */
  private static final int CELL_WIDTH_BITS = 3;
  /** Number of pixels across a cell. Must be a power of 2. */
  private static final int CELL_WIDTH = 1 << CELL_WIDTH_BITS;
  /** Mask to the cell width. */
  private static final int CELL_WIDTH_MASK = CELL_WIDTH - 1;
  /** Number of bits in half the cell width. */
  private static final int HALF_CELL_WIDTH_BITS = CELL_WIDTH_BITS - 1;
  /** Half the cell width. */
  private static final int HALF_CELL_WIDTH = 1 << HALF_CELL_WIDTH_BITS;
  /** Size of fixed-point arithmetic. */
  private static final int FIXED_POINTS_BITS = 16;
  /** Half in above fixed-point arithmetic. */
  private static final int HALF = 1 << (FIXED_POINTS_BITS - 1);
  /** Special axis shift. */
  private static final int SPECIAL_SHIFT = FIXED_POINTS_BITS + CELL_WIDTH_BITS;
  /** Special mask shift. */
  private static final int SPECIAL_MASK_SHIFT = FIXED_POINTS_BITS - CELL_WIDTH_BITS;
  /** Special mask for cell width in fixed point arithmetic. */
  private static final int FP_CELL_WIDTH_MASK = CELL_WIDTH_MASK << FIXED_POINTS_BITS;

  /** The world this instance applies to. */
  private final World mWorld;
  /** Width of world in cells. */
  private final int mWidth;

  /**
   * Construct a new line of sight mechanism for the specified World.
   *
   * @param world the world in which line of sight determinations should be made
   */
  public LineOfSight(final World world) {
    mWorld = world;
    mWidth = mWorld.width();
  }

  /**
   * Get the line of sight mask for a cell. The second parameter is currently
   * only necessary to handle magic glass.
   *
   * @param cell cell to get mask for
   * @param sourceOwner the owner of the source
   * @return the 8&times;8 packed mask
   */
  private long getLOSMask(final int cell, final int sourceOwner) {
    final Actor a = mWorld.actor(cell);
    if (a instanceof MagicGlass && a.getOwner() == sourceOwner) {
      return 0;
    }
    return a == null ? 0L : a.getState() == State.DEAD ? 0L : a.getLosMask();
  }

  /**
   * Test if there is line of sight from the source cell to the target cell.
   * Uses fixed-point arithmetic to avoid conditional branches in the central
   * loops. Special case code for horizontal and vertical lines (these can
   * be done way faster than general lines).  Only checks for cell level
   * automatic acceptance or rejection once per cell and avoids doing pixel
   * checks on automatically accepted cells.  Assumes source and target cells
   * are real cells.
   *
   * @param source source cell
   * @param target target cell
   * @return true if line of sight is possible, false otherwise
   */
  public boolean isLOS(final int source, final int target) {
    if (source == target) {
      return true; // quick exit for case of line-of-sight to self
    }
    final Actor sourceActor = mWorld.actor(source);
    final int sourceOwner = sourceActor == null ? -1 : sourceActor.getOwner();
    // get pixel coordinates of lower-left of each cell
    final int[] sxy = new int[2];
    mWorld.getCellCoordinates(source, sxy);
    final int osx = sxy[0] << CELL_WIDTH_BITS;
    final int osy = sxy[1] << CELL_WIDTH_BITS;
    final int[] txy = new int[2];
    mWorld.getCellCoordinates(target, txy);
    int tx = txy[0] << CELL_WIDTH_BITS;
    int ty = txy[1] << CELL_WIDTH_BITS;
    // adjust (sx,sy) to centre pixel of source closest to (tx,ty)
    // this is critical for correct functioning of what follows!
    int sx = osx + (osx >= tx ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH);
    int sy = osy + (osy >= ty ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH);
    // adjust (tx,ty) to centre pixel of target closest to (sx,sy)
    // less critical, but needed for technical correctness of slope
    tx += osx <= tx ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH;
    ty += osy <= ty ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH;
    // dy/dx is the slope of the line
    final int dx = tx - sx;
    final int dy = ty - sy;
    // handle special case of horizontal line, this is not strictly necessary because
    // subsequent code can handle this case, but included here for speed reasons
    if (dy == 0) {
      if (source < target) {
        for (int c = source + 1; c < target; ++c) {
          if ((getLOSMask(c, sourceOwner) & 0xFF00000000L) != 0L) {
            return false;
          }
        }
      } else {
        for (int c = target + 1; c < source; ++c) {
          if ((getLOSMask(c, sourceOwner) & 0xFF00000000L) != 0L) {
            return false;
          }
        }
      }
      return true;
    }
    // handle special case of vertical line, this is not strictly necessary because
    // subsequent code can handle this case, but included here for speed reasons
    if (dx == 0) {
      if (source < target) {
        for (int c = source + mWidth; c < target; c += mWidth) {
          if ((getLOSMask(c, sourceOwner) & 0x0808080808080808L) != 0L) {
            return false;
          }
        }
      } else {
        for (int c = target + mWidth; c < source; c += mWidth) {
          if ((getLOSMask(c, sourceOwner) & 0x0808080808080808L) != 0L) {
            return false;
          }
        }
      }
      return true;
    }
    int cell;
    // choose loop based on axis with greatest delta, tests abs(dx) >= abs(dy)
    if ((dx >= 0 ? dx : -dx) >= (dy >= 0 ? dy : -dy)) {
      // x-axis is the longer
      final int slope = (dy << FIXED_POINTS_BITS) / dx;
      if (dx >= 0) {
        sy = (sy << FIXED_POINTS_BITS) + HALF + (slope << HALF_CELL_WIDTH_BITS);
        sx += HALF_CELL_WIDTH;
        while ((cell = (sy >>> SPECIAL_SHIFT) * mWidth + (sx >>> CELL_WIDTH_BITS)) != target) {
          final long mask = getLOSMask(cell, sourceOwner);
          final int csx = sx, csy = sy;
          if (mask == ~0L) {
            return false;
          } else if (mask == 0L) {
            while (((csx ^ ++sx) | ((csy ^ (sy += slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH) {
              // DO NOTHING
            }
          } else {
            int xbit = 64 - (sx & CELL_WIDTH_MASK);
            do {
              if ((mask & (1L << (xbit-- - ((sy & FP_CELL_WIDTH_MASK) >>> SPECIAL_MASK_SHIFT)))) != 0L) {
                return false;
              }
            } while (((csx ^ ++sx) | ((csy ^ (sy += slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
          }
        }
      } else {
        sy = (sy << FIXED_POINTS_BITS) + HALF - (slope << HALF_CELL_WIDTH_BITS);
        sx -= HALF_CELL_WIDTH;
        while ((cell = (sy >>> SPECIAL_SHIFT) * mWidth + (sx >>> CELL_WIDTH_BITS)) != target) {
          final long mask = getLOSMask(cell, sourceOwner);
          final int csx = sx, csy = sy;
          if (mask == ~0L) {
            return false;
          } else if (mask == 0L) {
            while (((csx ^ --sx) | ((csy ^ (sy -= slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH) {
              // DO NOTHING
            }
          } else {
            int xbit = 64 - (sx & CELL_WIDTH_MASK);
            do {
              if ((mask & (1L << (xbit++ - ((sy & FP_CELL_WIDTH_MASK) >>> SPECIAL_MASK_SHIFT)))) != 0L) {
                return false;
              }
            } while (((csx ^ --sx) | ((csy ^ (sy -= slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
          }
        }
      }
    } else {
      // y-axis is the longer
      final int slope = (dx << FIXED_POINTS_BITS) / dy;
      if (dy >= 0) {
        sx = (sx << FIXED_POINTS_BITS) + HALF + (slope << HALF_CELL_WIDTH_BITS);
        sy += HALF_CELL_WIDTH;
        while ((cell = (sy >>> CELL_WIDTH_BITS) * mWidth + (sx >>> SPECIAL_SHIFT)) != target) {
          final long mask = getLOSMask(cell, sourceOwner);
          final int csx = sx, csy = sy;
          if (mask == ~0L) {
            return false;
          } else if (mask == 0L) {
            while (((csy ^ ++sy) | ((csx ^ (sx += slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH) {
              // DO NOTHING
            }
          } else {
            int ybit = 63 - ((sy & CELL_WIDTH_MASK) << CELL_WIDTH_BITS);
            do {
              if ((mask & (1L << (ybit - ((sx >>> FIXED_POINTS_BITS) & CELL_WIDTH_MASK)))) != 0L) {
                return false;
              }
              ybit -= CELL_WIDTH;
            } while (((csy ^ ++sy) | ((csx ^ (sx += slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
          }
        }
      } else {
        sx = (sx << FIXED_POINTS_BITS) + HALF - (slope << HALF_CELL_WIDTH_BITS);
        sy -= HALF_CELL_WIDTH;
        while ((cell = (sy >>> CELL_WIDTH_BITS) * mWidth + (sx >>> SPECIAL_SHIFT)) != target) {
          final long mask = getLOSMask(cell, sourceOwner);
          final int csx = sx, csy = sy;
          if (mask == ~0L) {
            return false;
          } else if (mask == 0L) {
            while (((csy ^ --sy) | ((csx ^ (sx -= slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH) {
              // DO NOTHING
            }
          } else {
            int ybit = 63 - ((sy & CELL_WIDTH_MASK) << CELL_WIDTH_BITS);
            do {
              if ((mask & (1L << (ybit - ((sx >>> FIXED_POINTS_BITS) & CELL_WIDTH_MASK)))) != 0L) {
                return false;
              }
              ybit += CELL_WIDTH;
            } while (((csy ^ --sy) | ((csx ^ (sx -= slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
          }
        }
      }
    }
    return true;
  }

  /*
    This version includes advanced cell stepping, but it is only faster
    for CELL_WIDTH_BITS > 3.
  */
  /*
  public boolean isLOS(final int source, final int target) {
    // quick exit for case of line-of-sight to self
    if (source == target) return true;

    // get pixel coordinates of lower-left of each cell
    final int[] sxy = new int[2];
    mWorld.getCellCoordinates(source, sxy);
    final int osx = sxy[0] << CELL_WIDTH_BITS;
    final int osy = sxy[1] << CELL_WIDTH_BITS;
    final int[] txy = new int[2];
    mWorld.getCellCoordinates(target, txy);
    int tx = txy[0] << CELL_WIDTH_BITS;
    int ty = txy[1] << CELL_WIDTH_BITS;

    // adjust (sx,sy) to centre pixel of source closest to (tx,ty)
    // this is critical for correct functioning of what follows!
    final int sx = osx + (osx >= tx ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH);
    final int sy = osy + (osy >= ty ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH);
    // adjust (tx,ty) to centre pixel of target closest to (sx,sy)
    // less critical, but needed for technical correctness of slope
    tx += osx <= tx ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH;
    ty += osy <= ty ? HALF_CELL_WIDTH - 1 : HALF_CELL_WIDTH;

    // dy/dx is the slope of the line
    final int dx = tx - sx;
    final int dy = ty - sy;

    // handle special case of horizontal line, this is not strictly
    // necessary because subsequent code can handle this case, but
    // this special case can be done extremely quickly.
    if (dy == 0) {
      if (source < target) {
        for (final int c = source + 1; c < target; ++c) {
          if ((getLOSMask(c) & 0xFF00000000L) != 0L) {
            return false;
          }
        }
      } else {
        for (final int c = target + 1; c < source; ++c) {
          if ((getLOSMask(c) & 0xFF00000000L) != 0L) {
            return false;
          }
        }
      }
      return true;
    }

    // handle special case of vertical line, this is not strictly
    // necessary because subsequent code can handle this case, but
    // this special case can be done extremely quickly.
    if (dx == 0) {
      if (source < target) {
        for (final int c = source + mWidth; c < target; c += mWidth) {
          if ((getLOSMask(c) & 0x0808080808080808L) != 0L) {
            return false;
          }
        }
      } else {
        for (final int c = target + mWidth; c < source; c += mWidth) {
          if ((getLOSMask(c) & 0x0808080808080808L) != 0L) {
            return false;
          }
        }
      }
      return true;
    }

    final int cell;
    // choose loop based on axis with greatest delta, tests abs(dx) >= abs(dy)
    if ((dx >= 0 ? dx : -dx) >= (dy >= 0 ? dy : -dy)) {
      // x-axis is the longer
      final int slope = (dy << FIXED_POINTS_BITS) / dx;
      if (dx >= 0) {
        // rightwards
        sy = (sy << FIXED_POINTS_BITS) + HALF + (slope << HALF_CELL_WIDTH_BITS);
        sx += HALF_CELL_WIDTH;
        while ((cell = (sy >>> SPECIAL_SHIFT) * mWidth + (sx >>> CELL_WIDTH_BITS)) != target) {
          final long mask = getLOSMask(cell);
          final int csx = sx, csy = sy;
          if (mask == ~0L) {
            return false;
          } else if (mask == 0L) {
            if ((sx & CELL_WIDTH_MASK) == 0) {
              final int fy = sy & FP_CELL_FRACTION;
              sx += 1 + (slope <= 0 ? -fy : FP_CELL_WIDTH - fy) / slope;
              if (sx - csx < CELL_WIDTH) {
                sy += (sx - csx) * slope;
              } else {
                sy += CELL_WIDTH * slope;
                sx = csx + CELL_WIDTH;
              }
            } else {
              sx = (sx + CELL_WIDTH) & ~CELL_WIDTH_MASK;
              sy += (sx - csx) * slope;
            }
          } else {
            final int xbit = 64 - (sx & CELL_WIDTH_MASK);
            do {
              if ((mask & (1L << (xbit-- - ((sy & FP_CELL_WIDTH_MASK) >>> SPECIAL_MASK_SHIFT)))) != 0L) {
                return false;
              }
            } while (((csx ^ ++sx) | ((csy ^ (sy += slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
          }
        }
      } else {
        // leftwards
        sy = (sy << FIXED_POINTS_BITS) + HALF - (slope << HALF_CELL_WIDTH_BITS);
        sx -= HALF_CELL_WIDTH;
        while ((cell = (sy >>> SPECIAL_SHIFT) * mWidth + (sx >>> CELL_WIDTH_BITS)) != target) {
          final long mask = getLOSMask(cell);
          final int csx = sx, csy = sy;
          if (mask == ~0L) {
            return false;
          } else if (mask == 0L) {
            if ((sx & CELL_WIDTH_MASK) == CELL_WIDTH_MASK) {
              final int fy = sy & FP_CELL_FRACTION;
              sx += (slope >= 0 ? -fy : FP_CELL_WIDTH - fy) / slope - 1;
              if (csx - sx < CELL_WIDTH) {
                sy -= (csx - sx) * slope;
              } else {
                sy -= CELL_WIDTH * slope;
                sx = csx - CELL_WIDTH;
              }
            } else {
              sx = (sx & ~CELL_WIDTH_MASK) - 1;
              sy -= (csx - sx) * slope;
            }
          } else {
            final int xbit = 64 - (sx & CELL_WIDTH_MASK);
            do {
              if ((mask & (1L << (xbit++ - ((sy & FP_CELL_WIDTH_MASK) >>> SPECIAL_MASK_SHIFT)))) != 0L) {
                return false;
              }
            } while (((csx ^ --sx) | ((csy ^ (sy -= slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
          }
        }
      }
    } else {
      // y-axis is the longer
      final int slope = (dx << FIXED_POINTS_BITS) / dy;
      if (dy >= 0) {
        // downwards
        sx = (sx << FIXED_POINTS_BITS) + HALF + (slope << HALF_CELL_WIDTH_BITS);
        sy += HALF_CELL_WIDTH;
        while ((cell = (sy >>> CELL_WIDTH_BITS) * mWidth + (sx >>> SPECIAL_SHIFT)) != target) {
          final long mask = getLOSMask(cell);
          final int csx = sx, csy = sy;
          if (mask == ~0L) {
            return false;
          } else if (mask == 0L) {
            if ((sy & CELL_WIDTH_MASK) == 0) {
              final int fx = sx & FP_CELL_FRACTION;
              sy += 1 + (slope <= 0 ? -fx : FP_CELL_WIDTH - fx) / slope;
              if (sy - csy < CELL_WIDTH) {
                sx += (sy - csy) * slope;
              } else {
                sx += CELL_WIDTH * slope;
                sy = csy + CELL_WIDTH;
              }
            } else {
              sy = (sy + CELL_WIDTH) & ~CELL_WIDTH_MASK;
              sx += (sy - csy) * slope;
            }
          } else {
            final int ybit = 63 - ((sy & CELL_WIDTH_MASK) << CELL_WIDTH_BITS);
            do {
              if ((mask & (1L << (ybit - ((sx >>> FIXED_POINTS_BITS) & CELL_WIDTH_MASK)))) != 0L) {
                return false;
              }
              ybit -= CELL_WIDTH;
            } while  (((csy ^ ++sy) | (csx ^ (sx += slope))) < CELL_WIDTH);
          }
        }
      } else {
        // upwards
        sx = (sx << FIXED_POINTS_BITS) + HALF - (slope << HALF_CELL_WIDTH_BITS);
        sy -= HALF_CELL_WIDTH;
        while ((cell = (sy >>> CELL_WIDTH_BITS) * mWidth + (sx >>> SPECIAL_SHIFT)) != target) {
          final long mask = getLOSMask(cell);
          final int csx = sx, csy = sy;
          if (mask == ~0L) {
            return false;
          } else if (mask == 0L) {
            if ((sy & CELL_WIDTH_MASK) == CELL_WIDTH_MASK) {
              final int fx = sx & FP_CELL_FRACTION;
              sy += (slope >= 0 ? -fx : FP_CELL_WIDTH - fx) / slope - 1;
              if (csy - sy < CELL_WIDTH) {
                sx -= (csy - sy) * slope;
              } else {
                sx -= CELL_WIDTH * slope;
                sy = csy - CELL_WIDTH;
              }
            } else {
              sy = (sy & ~CELL_WIDTH_MASK) - 1;
              sx -= (csy - sy) * slope;
            }
          } else {
            final int ybit = 63 - ((sy & CELL_WIDTH_MASK) << CELL_WIDTH_BITS);
            do {
              if ((mask & (1L << (ybit - ((sx >>> FIXED_POINTS_BITS) & CELL_WIDTH_MASK)))) != 0L) {
                return false;
              }
              ybit += CELL_WIDTH;
            } while  (((csy ^ --sy) | ((csx ^ (sx -= slope)) >>> FIXED_POINTS_BITS)) < CELL_WIDTH);
          }
        }
      }
    }
    return true;
  }
  */
}
