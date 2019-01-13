package servant.servantandroid.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import servant.servantandroid.R;

public class ErrorFragment extends DialogFragment {

    private static final String ERROR_ARG   = "error";
    private static final String DETAILS_ARG = "details";

    public static void showErrorFragment(FragmentManager manager, String error, String details) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ERROR_ARG, error);
        arguments.putString(DETAILS_ARG, details);

        fragment.setArguments(arguments);
        fragment.show(manager, null);
    }

    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstance) {
        if(getArguments() == null) throw new IllegalArgumentException(
            "an error-dialog was opened without any arguments... lol"
        );

        String error   = getArguments().getString(ERROR_ARG);
        String details = getArguments().getString(DETAILS_ARG);

        error   = (error   == null? getString(R.string.not_available) : error);
        details = (details == null? getString(R.string.not_available) : details);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View errorDialogLayout = inflater.inflate(R.layout.error_dialog, null);

        ((TextView)errorDialogLayout.findViewById(R.id.error_description)).setText(error);
        ((TextView)errorDialogLayout.findViewById(R.id.error_details)).setText(details);

        // make the error details scrollable and use as closure in the neutral button lambda
        final TextView detailsView = errorDialogLayout.findViewById(R.id.error_details);
        detailsView.setMovementMethod(new ScrollingMovementMethod());

        return new AlertDialog.Builder(getActivity()).setView(errorDialogLayout)
            .setNeutralButton(R.string.ok, (dialog, id) -> dialog.dismiss())
            .create();
    }
}
