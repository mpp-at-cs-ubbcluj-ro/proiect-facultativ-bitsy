using Android.App;
using Android.Views;
using Android.Widget;
using HelpingHands.Data;
using System.Collections.Generic;

namespace HelpingHands.Adapters
{
    internal class ParticipantAdapter : BaseAdapter<Participant>
    {
        List<Participant> Items;
        Activity Context;
        public ParticipantAdapter(Activity context, List<Participant> items) : base()
        {
            this.Context = context;
            this.Items = items;
        }


        public override Participant this[int position] => Items[position];

        public override int Count => Items.Count;

        public override long GetItemId(int position) => position;

        private static string AutoEllipsis(string s, int cnt)
        {
            if (s.Length < cnt) return s;
            return s.Substring(cnt-2) + "...";
        }

        public override View GetView(int position, View convertView, ViewGroup parent)
        {
            var item = Items[position];
            View view = convertView;
            if (view == null) // no view to re-use, create new
                view = Context.LayoutInflater.Inflate(Resource.Layout.participant_row, null);
            view.FindViewById<TextView>(Resource.Id.NumePrenumeBox).Text = item.Voluntar.Nume + " " + item.Voluntar.Prenume;
            view.FindViewById<TextView>(Resource.Id.OranizerBox).Visibility = item.IsOrganizer ? ViewStates.Visible : ViewStates.Invisible;            
            return view;
        }
    }
}