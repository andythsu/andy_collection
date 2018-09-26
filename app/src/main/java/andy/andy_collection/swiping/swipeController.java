package andy.andy_collection.swipeing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import static android.support.v7.widget.helper.ItemTouchHelper.*;


public class swipeController extends Callback {

    enum ButtonsState {
        GONE,
        LEFT_VISIBLE,
        RIGHT_VISIBLE
    }

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private static RectF editBtnInstance = null;

    private static RectF deleteBtnInstance = null;

    private static RecyclerView.ViewHolder currentItemViewHolder = null;

    private static final float BUTTON_WIDTH = 150;

    private static final float MARGIN = 10;

    private static final float NUM_OF_BTNS = 2;

    private static swipeActions buttonAction;

    public swipeController(swipeActions action) {
        this.buttonAction = action;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT | RIGHT);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        currentItemViewHolder = viewHolder;

        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
//                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, BUTTON_WIDTH);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE)
                    dX = Math.min(dX, -BUTTON_WIDTH * NUM_OF_BTNS - MARGIN);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -BUTTON_WIDTH) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
//                    else if (dX > BUTTON_WIDTH) buttonShowedState = ButtonsState.LEFT_VISIBLE;

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }

    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    swipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;
                    if (buttonAction != null) {
                        if (editBtnInstance != null && editBtnInstance.contains(event.getX(), event.getY())) {
                            if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                                // edit button clicked
                                buttonAction.onClicked("edit", viewHolder.getAdapterPosition());
                            }
                        } else if (deleteBtnInstance != null && deleteBtnInstance.contains(event.getX(), event.getY())) {
                            if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                                buttonAction.onClicked("delete", viewHolder.getAdapterPosition());
                            }
                        }
                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private static void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
//        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF editBtn = new RectF(itemView.getRight() - BUTTON_WIDTH * NUM_OF_BTNS, itemView.getTop(), itemView.getRight() - BUTTON_WIDTH, itemView.getBottom());
        p.setColor(Color.BLUE);
        c.drawRoundRect(editBtn, corners, corners, p);
        drawText("EDIT", c, editBtn, p);

        RectF deleteBtn = new RectF(editBtn.right + MARGIN, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        p.setColor(Color.RED);
        c.drawRoundRect(deleteBtn, corners, corners, p);
        drawText("DELETE", c, deleteBtn, p);

//        buttonInstance = null;
//        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
//            buttonInstance = editBtn;
//        } else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
//            buttonInstance = deleteBtn;
//        }

        editBtnInstance = editBtn;
        deleteBtnInstance = deleteBtn;


    }

    private static void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 30;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
    }

    public static void onDraw(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder);
        }
    }
}