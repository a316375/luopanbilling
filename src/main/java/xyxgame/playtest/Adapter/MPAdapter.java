package xyxgame.playtest.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.SkuDetails;

import java.util.List;

import xyxgame.playtest.Interface.ListOnClik;
import xyxgame.playtest.MainActivity;
import xyxgame.playtest.R;

public class MPAdapter extends RecyclerView.Adapter<MPAdapter.Holer> {
    MainActivity mainActivity;
    List<SkuDetails> skuDetails;
    BillingClient billingClient;

    public MPAdapter(MainActivity mainActivity, List<SkuDetails> skuDetails, BillingClient billingClient) {
        this.mainActivity = mainActivity;
        this.skuDetails = skuDetails;
        this.billingClient = billingClient;
    }

    @NonNull
    @Override
    public Holer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mainActivity.getApplicationContext()).inflate(R.layout.layout_item,parent,false);

        return new Holer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holer holder, int position) {
       holder.textView.setText(skuDetails.get(position).getTitle());
       holder.setListOnClik(new ListOnClik() {
           @Override
           public void OnListener(View view, int point) {
               BillingFlowParams billingFlowParams=BillingFlowParams.newBuilder().setSkuDetails(skuDetails.get(point)).build();
               billingClient.launchBillingFlow(mainActivity,billingFlowParams);
           }
       });
    }

    @Override
    public int getItemCount() {
        return skuDetails.size();
    }

    public class Holer extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;

        ListOnClik listOnClik;

        public void setListOnClik(ListOnClik listOnClik) {
            this.listOnClik = listOnClik;
        }

        public Holer(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listOnClik.OnListener(view,getAdapterPosition());
        }
    }
}
