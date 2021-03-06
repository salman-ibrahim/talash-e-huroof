

package com.gatow.salman.talashehuroof.views.board

import android.content.Context
import android.graphics.Paint
import android.media.MediaPlayer
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.gatow.salman.talashehuroof.R
import com.gatow.salman.talashehuroof.models.BoardCharacter
import com.gatow.salman.talashehuroof.presenter.board.BoardPresenter
import com.gatow.salman.talashehuroof.utils.LineSelectionHelper
import com.gatow.salman.talashehuroof.utils.bounceAnimation
import com.gatow.salman.talashehuroof.utils.dragListener.DragSelectReceiver
import com.gatow.salman.talashehuroof.utils.dragListener.DragSelectTouchListener
import com.gatow.salman.talashehuroof.utils.dragListener.Mode
import kotlinx.android.synthetic.main.item_board.view.*


/**
 * Characters Board Adapter to the RecycleView
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class BoardAdapter(
    var context: Context? = null,
    private val presenter: BoardPresenter,
    val boardSize: Int = 10,
    val isTest: Boolean = false
) :
    RecyclerView.Adapter<BoardAdapter.ViewHolder>(), DragSelectReceiver {

    var grid = ArrayList<ArrayList<BoardCharacter>>()
    val touchListener = DragSelectTouchListener.create(context!!, this) {
        disableAutoScroll()
        mode = Mode.PATH
    }
    var lineSelectHelper: LineSelectionHelper? = null

    /** Update any changed into the grid **/
    fun updateGrid(grid: ArrayList<ArrayList<BoardCharacter>>) {
        this.grid = grid
        notifyDataSetChanged()
        touchListener.setIsActive(true)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnItemTouchListener(touchListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context),
            parent
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(
            grid[position % boardSize][if (position < boardSize) 0 else position / boardSize],
            position,
            isTest
        )

    override fun getItemCount(): Int = grid.size * grid.size
    override fun isIndexSelectable(index: Int) = true

    override fun isSelected(index: Int) =
        grid[index % boardSize][if (index < boardSize) 0 else index / boardSize].isOnSelection

    /**
     * Release selection of all items
     */
    override fun releaseSelection() {
        lineSelectHelper = null
        presenter.checkForWord()
        notifyDataSetChanged()
        touchListener.setIsActive(true)
    }

    /**
     * Set selected items to paint the right words on the board
     */
    override fun setSelected(index: Int, selected: Boolean) {
        val touchLetter = grid[index % boardSize][index / boardSize]
        if (lineSelectHelper == null) {
            lineSelectHelper =
                LineSelectionHelper()
            lineSelectHelper?.addFirst(touchLetter, grid)
            touchLetter.isOnSelection = true
            presenter.addCharacter(touchLetter)
            notifyItemChanged(index)
        } else {
            presenter.selectedWord.forEach { it.isOnSelection = false }
            presenter.selectedWord.clear()
            lineSelectHelper?.showLine(touchLetter)?.forEach { boardToSelect ->
                boardToSelect.isOnSelection = true
                presenter.addCharacter(boardToSelect)
//                notifyItemChanged(getGridIndex(boardToSelect))
            }
            notifyDataSetChanged()
            playNote(context, grid)
        }
    }

    /**
     * Play sound based on many elements are selected in the board
     * @param ctx context used to retrieve and play the sound from resources
     * @param grid gaming board
     */
    fun playNote(ctx: Context?, grid: ArrayList<ArrayList<BoardCharacter>>) {

        if (grid.isEmpty() || ctx == null) return

        var count = 0
        grid.forEach { it.forEach { count += if (it.isOnSelection) 1 else 0 } }

        MediaPlayer.create(
            ctx, when (count) {
                1 -> R.raw.a3
                2 -> R.raw.c3
                3 -> R.raw.d3
                4 -> R.raw.e3
                5 -> R.raw.f3
                6 -> R.raw.g3
                7 -> R.raw.a4
                8 -> R.raw.c4
                9 -> R.raw.d4
                10 -> R.raw.e4
                11 -> R.raw.f4
                12 -> R.raw.g4
                13 -> R.raw.a5
                14 -> R.raw.c5
                15 -> R.raw.d5
                16 -> R.raw.e5
                17 -> R.raw.f5
                else -> R.raw.g5
            }
        ).apply {
            setOnPreparedListener { mediaToPlay ->
                mediaToPlay.setVolume(1f, 1f)
                mediaToPlay.setOnCompletionListener { mediaForRelease ->
                    mediaForRelease.release()
                }
                mediaToPlay.start()
            }
        }
    }

    override fun getItemId(position: Int) = position.toLong()
    override fun getItemViewType(position: Int) = 0

    /**
     * ViewHOlder that represent a character of the board table
     */
    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_board, parent, false)) {

        val boardElement = itemView.boardItem

        val selectedColor by lazy {
            AppCompatResources.getColorStateList(
                itemView.context,
                R.color.colorPrimary
            )
        }

        val foundWordColor by lazy {
            AppCompatResources.getColorStateList(
                itemView.context,
                R.color.whiteCharColor
            )
        }

        val unselectedColor by lazy {
            AppCompatResources.getColorStateList(
                itemView.context,
                R.color.unselectCharColor
            )
        }
        val removedLetterColor by lazy {
            AppCompatResources.getColorStateList(
                itemView.context,
                R.color.colorAccent
            )
        }

        /**
         * Bind BoardCharacter in the TextView
         */
        fun bind(boardCharacter: BoardCharacter, pos: Int, isTest: Boolean) {

            if (isTest && pos < 4)
                boardElement.contentDescription = "test-$pos"
            boardElement.text = boardCharacter.char

            if (boardCharacter.selected) {
                if (boardElement?.textColors != removedLetterColor) {
                    boardElement.setTextColor(foundWordColor)
                    boardElement.setBackgroundResource(R.drawable.selected_word_bg_rounded)
                }
            } else {
                if (boardCharacter.isOnSelection) {
                    if (boardElement.textColors != selectedColor) {
                        boardElement.setTextColor(selectedColor)
                        boardElement.isHapticFeedbackEnabled = true
                        boardElement.performHapticFeedback(
                            HapticFeedbackConstants.LONG_PRESS,
                            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                        )
                        bounceAnimation(boardElement)
                    }
                } else {
                    if (boardElement.textColors != unselectedColor)
                        boardElement.setTextColor(selectedColor)
                }
            }

            if (itemView.tag == null) {
                itemView.tag = boardCharacter
            }
        }
    }

}