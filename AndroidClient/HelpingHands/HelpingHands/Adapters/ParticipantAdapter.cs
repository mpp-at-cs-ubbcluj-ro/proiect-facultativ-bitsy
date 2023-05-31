using Android.App;
using Android.Views;
using Android.Widget;
using HelpingHands.Data;
using System.Collections.Generic;

namespace HelpingHands.Adapters
{
    internal class ParticipantAdapter : ListAdapter<Participant>
    {        
        public ParticipantAdapter(Activity context, List<Participant> items) : base(context, items)
        {        
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