using Android.App;
using Android.Views;
using Android.Widget;
using HelpingHands.Data;
using Java.IO;
using System.Collections.Generic;
using static HelpingHands.Adapters.ParticipantAdapter;

namespace HelpingHands.Adapters
{
    internal class ParticipantAdapter : ListAdapter<Participant>
    {
        bool IsEditable { get; }

        public ParticipantAdapter(Activity context, List<Participant> items, bool editable = false) : base(context, items)
        {
            IsEditable = editable;
        }        

        public override View GetView(int position, View convertView, ViewGroup parent)
        {
            if (!IsEditable) 
            {
                var item = Items[position];
                View view = convertView;
                if (view == null) // no view to re-use, create new
                    view = Context.LayoutInflater.Inflate(Resource.Layout.participant_row, null);
                view.FindViewById<TextView>(Resource.Id.NumePrenumeBox).Text = item.Voluntar.Nume + " " + item.Voluntar.Prenume;
                view.FindViewById<TextView>(Resource.Id.OranizerBox).Visibility = item.IsOrganizer ? ViewStates.Visible : ViewStates.Invisible;                
                return view;
            }
            else
            {
                System.Console.WriteLine("Participant item list");
                var item = Items[position];
                View view = convertView;
                if (view == null) // no view to re-use, create new
                    view = Context.LayoutInflater.Inflate(Resource.Layout.participant_row_editable, null);
                view.FindViewById<TextView>(Resource.Id.NumePrenumeBox).Text = item.Voluntar.Nume + " " + item.Voluntar.Prenume;
                view.FindViewById<TextView>(Resource.Id.OranizerBox).Visibility = item.IsOrganizer ? ViewStates.Visible : ViewStates.Invisible;
                view.FindViewById<ImageView>(Resource.Id.RemoveButton).Visibility = ViewStates.Visible;

                view.FindViewById<ImageView>(Resource.Id.RemoveButton).Click -= ParticipantAdapter_Click;
                view.FindViewById<ImageView>(Resource.Id.RemoveButton).Click += ParticipantAdapter_Click;
                view.FindViewById<ImageView>(Resource.Id.RemoveButton).Tag= item.Id;
                System.Console.WriteLine("Done?");
                return view;
            }
        }

        public delegate void OnRemoveButtonClick(ImageView sender, int participantId);
        public event OnRemoveButtonClick RemoveButtonClick;

        private void ParticipantAdapter_Click(object sender, System.EventArgs e)
        {
            var btn = sender as ImageView;
            var id = (int)btn.Tag;
            System.Console.WriteLine("Participant id = " + id);
            System.Console.WriteLine(btn.Tag);

            RemoveButtonClick?.Invoke(btn, id);
        }
    }
}