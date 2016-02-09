package fyber.jehandadk.com.offers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fyber.jehandadk.com.R;
import fyber.jehandadk.com.base.adapters.ListAdapter;
import fyber.jehandadk.com.data.models.Offer;
import fyber.jehandadk.com.helpers.Utils;

/**
 * Created by jehandad.kamal on 1/24/2016.
 */
public class OffersAdapter extends ListAdapter<OffersAdapter.ProductHolder, Offer> {

    ItemClickedListener listener;

    public OffersAdapter(List<Offer> list, ItemClickedListener listener) {
        super(list);
        this.listener = listener;
        setHasStableIds(true);
    }

    public OffersAdapter(ItemClickedListener listener) {
        this(new ArrayList<>(), listener);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_offer, null));
    }

    @Override
    public void onBindViewHolder(ProductHolder holder, Offer offer) {
        Glide.with(holder.itemView.getContext()).load(offer.getThumbnail().getHires())
                .placeholder(R.drawable.img_placeholder_loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(holder.imgProduct);
        holder.txtProductAmount.setText(Utils.formatCurrency(offer.getPayout()));
        holder.txtProductTitle.setText(offer.getTitle());
        holder.txtProductTeaser.setText(offer.getTeaser());
    }

    public interface ItemClickedListener {
        void onItemClicked(Offer Offer);
    }


    public class ProductHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_product)
        public ImageView imgProduct;
        @Bind(R.id.txt_product_title)
        public TextView txtProductTitle;
        @Bind(R.id.txt_product_teaser)
        public TextView txtProductTeaser;
        @Bind(R.id.txt_product_amount)
        public TextView txtProductAmount;

        public ProductHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                Offer Offer = list.get(getAdapterPosition());
                listener.onItemClicked(Offer);
            });
        }
    }
}
