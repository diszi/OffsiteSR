package hu.d2.offsitesr.ui.view.component;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.d2.offsitesr.R;
import hu.d2.offsitesr.ui.model.Asset;
import hu.d2.offsitesr.ui.model.AssetUserCust;

/**
 * Created by szidonia.laszlo on 2017. 11. 02..
 *
 *  Dialog will show after click on image button (zoom) in ticket details page
 */

public class AssetDetailsDialog extends DialogFragment {

    public static String SERIALIZABLE_NAME = "List_Serializable";


    @BindView(R.id.diagAsset_cancelButton)
    Button cancelButton;

    @BindView(R.id.assetDetails_Num)
    TextView assetDetails_Num;

    @BindView(R.id.assetDetails_description)
    TextView assetDetails_Description;

    @BindView(R.id.assetDetails_Location)
    TextView assetDetails_Location;

    @BindView(R.id.assetDetails_Status)
    TextView assetDetails_Status;

    @BindView(R.id.assetDetails_Pluspcustomer)
    TextView assetDetails_Pluspcustomer;

    @BindView(R.id.assetDetails_SerialNum)
    TextView assetDetails_SerialNum;

    @BindView(R.id.assetDetails_Custodian)
    TextView assetDetails_Custodian;

    @BindView(R.id.assetDetails_User)
    TextView assetDetails_User;

    @BindView(R.id.diagAsset_title)
    TextView title;

    private Asset assetObj ;
    private List<AssetUserCust> assetUCList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assetObj = (Asset) getArguments().getSerializable(AssetDetailsDialog.SERIALIZABLE_NAME);
            assetUCList = assetObj.getAssetUserCustList();
        }
    }

    /**
     *
     * @param inflater  - inflate view in the fragment (dialog_asset_details.xml)
     * @param container  - parent view
     * @param savedInstanceState - fragment is being reconstructed using this param
     * @return - a View for the fragment's UI
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View contentView = inflater.inflate(R.layout.dialog_asset_details, container, false);

        ButterKnife.bind(this, contentView);

        cancelButton.setOnClickListener((view -> {
            dismiss();
        }));

        title.setText(getString(R.string.dialogAsset_title));

        assetDetails_Num.setText(assetObj.getAssetNum());
        assetDetails_Description.setText(assetObj.getDescription());
        assetDetails_Location.setText(assetObj.getLocation());
        assetDetails_Status.setText(assetObj.getStatus());
        assetDetails_Pluspcustomer.setText(assetObj.getPluspCustomer());
        assetDetails_SerialNum.setText(assetObj.getSerialNum());



        for (int i=0;i< assetUCList.size();i++)
        {

            if (assetUCList.get(i).isUser()){
                assetDetails_User.setText(assetUCList.get(i).getPersonID());
             }else
                 if (assetUCList.get(i).isCustodian()){
                     assetDetails_Custodian.setText(assetUCList.get(i).getPersonID());
                 }
        }

        return contentView;
    }


}
