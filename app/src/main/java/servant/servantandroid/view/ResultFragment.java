package servant.servantandroid.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import servant.servantandroid.R;
import servant.servantandroid.databinding.ResultLayoutBinding;
import servant.servantandroid.internal.api_mirror.Capability;
import servant.servantandroid.internal.api_mirror.Result;

public class ResultFragment extends DialogFragment {
    private static final String MESSAGE_ARG = "result_message";
    private static final String TYPE_ARG    = "result_type";

    private static final String FULLNAME_ARG = "capability_fullname";

    public static void showResultFragment(FragmentManager manager, Result result, Capability capability) {
        ResultFragment fragment = new ResultFragment();
        Bundle arguments = new Bundle();

        arguments.putString(MESSAGE_ARG, result.getMessage());
        arguments.putString(TYPE_ARG, result.getType().name());

        arguments.putString(FULLNAME_ARG, capability.getFullname());

        fragment.setArguments(arguments);
        fragment.show(manager, null);
    }

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstance) {
        if(getArguments() == null) throw new IllegalArgumentException(
            "a result-dialog was opened without any arguments... lol"
        );

        String message = getArguments().getString(MESSAGE_ARG);
        String type    = getArguments().getString(TYPE_ARG);

        String fullname = getArguments().getString(FULLNAME_ARG);

        if(message == null || type == null) throw new IllegalArgumentException(
            "a result dialog was opened with a missing argument. report this to the servant github"
        );

        ResultLayoutBinding binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            R.layout.result_layout, null, false
        );

        binding.capabilityFullname.setText(fullname);
        binding.resultType.setText(type);
        binding.resultMessage.setText(message);

        return new AlertDialog.Builder(getActivity())
            .setView(binding.getRoot())
            .setPositiveButton(R.string.ok, (dialog, id) -> dialog.dismiss())
            .create();
    }
}
