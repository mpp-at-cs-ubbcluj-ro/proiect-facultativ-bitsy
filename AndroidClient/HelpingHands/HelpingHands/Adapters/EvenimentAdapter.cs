using Android.App;
using Android.Views;
using Android.Widget;
using HelpingHands.Data;
using System.Collections.Generic;

namespace HelpingHands.Adapters
{
    internal class EvenimentAdapter : BaseAdapter<Eveniment>
    {
        List<Eveniment> Items;
        Activity Context;
        public EvenimentAdapter(Activity context, List<Eveniment> items) : base()
        {
            this.Context = context;
            this.Items = items;
        }


        public override Eveniment this[int position] => Items[position];

        public override int Count => Items.Count;

        public override long GetItemId(int position) => position;        

        public override View GetView(int position, View convertView, ViewGroup parent)
        {
            var item = Items[position];
            View view = convertView;
            if (view == null) // no view to re-use, create new
                view = Context.LayoutInflater.Inflate(Resource.Layout.eveniment_row, null);

            view.FindViewById<TextView>(Resource.Id.Text1).Text = item.Name;
            view.FindViewById<TextView>(Resource.Id.Text2).Text = item.Description;

            //var imageBitmap = GetImageBitmapFromUrl(item.ImageURI);
            //view.FindViewById<ImageView>(Resource.Id.Image).SetImageBitmap(imageBitmap);
            return view;
        }
    }
}