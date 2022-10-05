package com.allexamnotes.app.fragment.comments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.allexamnotes.app.Config;
import com.allexamnotes.app.ContainerActivity;
import com.allexamnotes.app.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.allexamnotes.libdroid.model.comment.Comment;
import com.allexamnotes.libdroid.repo.CommentRepository;

public class ReplyCommentDialogFragment extends BottomSheetDialogFragment {

    public static final String TAG = "ReplyBottomSheetDialogFragment";

    private MaterialButton replyButton,closeBtn,signInBtn;
    private TextView bsTitle;
    private TextInputLayout contentTextInput,emailTextInput,nameTextInput;

    private String title;
    private int parentId;
    private int postId;

    private String email,name,content;

    private String token;
    private CardView sendingCommentAnimation;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;

    public static ReplyCommentDialogFragment newInstance(int postId,int parent, String title){
        ReplyCommentDialogFragment fragment = new ReplyCommentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("postId",postId);
        bundle.putInt("commentId",parent);
        bundle.putString("title",title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(Config.FORCE_RTL){
            getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View  onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.reply_bottom_sheet, container, false);
        if (getArguments()!=null){
            title = getArguments().getString("title");
            parentId = getArguments().getInt("commentId");
            postId = getArguments().getInt("postId");
        }
        mAuth = FirebaseAuth.getInstance();
        signInBtn = rootView.findViewById(R.id.signInBtn);
        sendingCommentAnimation = rootView.findViewById(R.id.sendingCommentAnimation);


        nameTextInput = rootView.findViewById(R.id.name);
        contentTextInput = rootView.findViewById(R.id.content);
        emailTextInput = rootView.findViewById(R.id.email);

        try {
            sharedPreferences = getActivity().getSharedPreferences(Config.defaultSharedPref, Context.MODE_PRIVATE);
            if (sharedPreferences.getString("token", null) != null) {
                signInBtn.setVisibility(View.GONE);
                name = sharedPreferences.getString("name",null);
                email = sharedPreferences.getString("email",null);
                nameTextInput.getEditText().setText(name);
                emailTextInput.getEditText().setText(email);
            }else if(mAuth.getCurrentUser()!=null){
                signInBtn.setVisibility(View.GONE);
                nameTextInput.getEditText().setText(mAuth.getCurrentUser().getDisplayName());
                emailTextInput.getEditText().setText(mAuth.getCurrentUser().getEmail());
            }
        }catch (Exception e){

        }

        signInBtn.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), ContainerActivity.class);
                intent.putExtra("screen", "login");
                startActivity(intent);
        });

        bsTitle = rootView.findViewById(R.id.bsTitle);
        closeBtn = rootView.findViewById(R.id.closeBtn);
        replyButton = rootView.findViewById(R.id.postReplyBtn);
        replyButton.setOnClickListener(view -> postComment());
        closeBtn.setOnClickListener(view -> dismiss());
        if (title!=null){
            bsTitle.setText(String.format(getResources().getString(R.string.comment_reply_to_text),title));
        }else {
            bsTitle.setText(R.string.post_comment);
        }

        validator();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void postComment(){
        if (getData()) {
            sendingCommentAnimation.setVisibility(View.VISIBLE);
            LiveData<Comment> commentLiveData;
            CommentRepository commentRepository = new CommentRepository();

            if (sharedPreferences.getString("token", null) != null) {
                token = sharedPreferences.getString("token", null);
                commentLiveData = commentRepository.postComment(token,postId, parentId, content);
            } else {
                commentLiveData = commentRepository.postCommentAnonymously(postId, parentId, name, email, content);
            }

            commentLiveData.observe(this, comment -> {
                if (comment != null) {
                    sendingCommentAnimation.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                }else {
                    sendingCommentAnimation.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Error posting comment", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean getData(){
        if (!TextUtils.isEmpty(nameTextInput.getEditText().getText().toString()) && !TextUtils.isEmpty(emailTextInput.getEditText().getText().toString()) &&
        !TextUtils.isEmpty(contentTextInput.getEditText().getText().toString())){
            name = nameTextInput.getEditText().getText().toString();
            email = emailTextInput.getEditText().getText().toString();
            content = contentTextInput.getEditText().getText().toString();
            return true;
        }else {
            return false;
        }
    }

    private void validator(){
        try {
            emailTextInput.getEditText().setOnFocusChangeListener((view, b) -> {
                if (!b){
                    if (!TextUtils.isEmpty(emailTextInput.getEditText().getText().toString())){
                        emailTextInput.setError(null);
                    }else {
                        emailTextInput.setError(getResources().getString(R.string.email_error_message));
                    }
                }
            });
            contentTextInput.getEditText().setOnFocusChangeListener((view, b) -> {
                if (!b){
                    if (!TextUtils.isEmpty(contentTextInput.getEditText().getText().toString())){
                        contentTextInput.setError(null);
                    }else {
                        contentTextInput.setError(getResources().getString(R.string.email_error_message));
                    }
                }
            });
            nameTextInput.getEditText().setOnFocusChangeListener((view, b) -> {
                if (!b){
                    if (!TextUtils.isEmpty(nameTextInput.getEditText().getText().toString())){
                        nameTextInput.setError(null);
                    }else {
                        nameTextInput.setError(getResources().getString(R.string.email_error_message));
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), getResources().getString(R.string.error_comment_validation),Toast.LENGTH_SHORT).show();
        }
    }


}
