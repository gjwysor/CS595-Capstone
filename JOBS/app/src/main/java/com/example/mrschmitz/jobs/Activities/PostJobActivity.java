package com.example.mrschmitz.jobs.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mrschmitz.jobs.R;
import com.example.mrschmitz.jobs.database.Jobs;
import com.example.mrschmitz.jobs.pojos.Job;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
import com.synnapps.carouselview.CarouselView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.mrschmitz.jobs.R.layout.activity_post_job;

public class PostJobActivity extends AppCompatActivity implements TextWatcher {

    private static final int PLACE_PICKER_REQUEST = 199;
    private Job job;
    private List<Uri> images = new ArrayList<>();

    @BindView(R.id.carouselView)
    CarouselView carouselView;

    @BindView(R.id.job_title)
    EditText titleEditText;

    @BindView(R.id.description)
    EditText descriptionEditText;

    @BindView(R.id.location)
    EditText locationEditText;

    @BindView(R.id.paymentAmount)
    EditText paymentAmountEditText;

    @BindView(R.id.paymentType)
    Spinner paymentTypeSpinner;

    @BindView(R.id.paymentMethod)
    Spinner paymentMethodSpinner;

    @BindView(R.id.post_job)
    Button postJobButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_post_job);
        ButterKnife.bind(this);

        job = new Job();
        job.setPosterUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        setupValidation();
    }

    @OnClick(R.id.post_job)
    public void postJob() {
        job.setTitle(titleEditText.getText().toString());
        job.setDescription(descriptionEditText.getText().toString());
        job.setPaymentAmount(Double.parseDouble(paymentAmountEditText.getText().toString()));
        job.setPaymentType(paymentTypeSpinner.getSelectedItem().toString());
        job.setPaymentMethod(paymentMethodSpinner.getSelectedItem().toString());

        new MaterialDialog.Builder(this)
                .title("Posting Job")
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .show();

        Jobs.postJob(job, images, task -> {
            String message = task.isSuccessful() ? "Job posted" : "Failed to post job";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupValidation() {
        postJobButton.setEnabled(false);
        titleEditText.addTextChangedListener(this);
        descriptionEditText.addTextChangedListener(this);
        locationEditText.addTextChangedListener(this);
        paymentAmountEditText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        boolean hasTitle = StringUtils.isNotBlank(titleEditText.getText());
        boolean hasDescription = StringUtils.isNotBlank(descriptionEditText.getText());
        boolean hasLocation = StringUtils.isNotBlank(locationEditText.getText());
        boolean hasPaymentAmount = StringUtils.isNotBlank(paymentAmountEditText.getText());

        boolean isValid = hasTitle && hasDescription && hasLocation && hasPaymentAmount;
        postJobButton.setEnabled(isValid);
    }

    @OnClick(R.id.addImages)
    public void addImages() {
        FishBun.with(this)
                .MultiPageMode()
                .setActionBarColor(getColor(R.color.colorPrimary), getColor(R.color.colorPrimaryDark))
                .setMaxCount(3)
                .startAlbum();
    }

    @OnClick(R.id.location)
    public void setLocation() {
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            switch (requestCode) {
                case PLACE_PICKER_REQUEST:
                    onPlaceResult(PlacePicker.getPlace(this, data));
                    break;
                case Define.ALBUM_REQUEST_CODE:
                    List<Uri> images = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    onImagesResult(images);
                    break;
            }
        }
    }

    private void onPlaceResult(Place place) {
        String address = place.getAddress().toString();
        locationEditText.setText(address);
        job.setAddress(address);
        job.setLatitude(place.getLatLng().latitude);
        job.setLongitude(place.getLatLng().longitude);
    }

    private void onImagesResult(List<Uri> images) {
        this.images = images;
        carouselView.setImageListener((position, imageView) -> {
            imageView.setImageURI(images.get(position));
        });
        carouselView.setPageCount(images.size());
    }

}
