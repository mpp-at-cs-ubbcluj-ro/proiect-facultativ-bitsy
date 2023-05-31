using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using HelpingHands.Adapters;
using HelpingHands.Data;
using HelpingHands.Utils;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HelpingHands
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme")]
    internal class ViewEvenimentOrganizatorActivity : Activity
    {
        Eveniment Eveniment;

        EditText VolEvenimentName;
        EditText VolEvenimentDescription;
        TextView VolEvenimentDate;
        Button VolButtonAddParticipant;
        ListView ParticipantListView;

        List<Participant> Participants = new List<Participant>();

        protected override void OnCreate(Bundle savedInstanceState)
        {
            Console.WriteLine("HERE IN ORGANIZER");
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_view_eveniment_organizator);

            Eveniment = JsonConvert.DeserializeObject<Eveniment>(Intent.GetStringExtra("eveniment"));

            ParticipantListView = FindViewById<ListView>(Resource.Id.ParticipantListView);

            VolEvenimentName = FindViewById<EditText>(Resource.Id.VolEvenimentName);
            VolEvenimentDescription = FindViewById<EditText>(Resource.Id.VolEvenimentDescription);
            VolEvenimentDate = FindViewById<TextView>(Resource.Id.VolEvenimentDate);
            VolButtonAddParticipant = FindViewById<Button>(Resource.Id.VolButtonAddParticipant);

            VolEvenimentName.Text = Eveniment.Name;
            VolEvenimentDescription.Text = Eveniment.Description;
            VolEvenimentDate.Text = Eveniment.StartDate.ToString("dd.MM.yyyy") + " - " + Eveniment.EndDate.ToString("dd.MM.yyyy");

            VolButtonAddParticipant.Click += VolButtonAddParticipant_Click;

            ParticipantListView.SetOnTouchListener(new ListTouchListener());

            Task.Run(Load);
        }

        class ListTouchListener : Java.Lang.Object, ListView.IOnTouchListener
        {
            public bool OnTouch(View v, MotionEvent e)
            {
                var action = e.Action;
                switch (action)
                {
                    case MotionEventActions.Down:
                        // Disallow ScrollView to intercept touch events.
                        v.Parent.RequestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEventActions.Up:
                        // Allow ScrollView to intercept touch events.
                        v.Parent.RequestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle ListView touch events.
                v.OnTouchEvent(e);
                return true;
            }
        }

        async void Load()
        {
            try
            {
                RunOnUiThread(async () =>
                {
                    Console.WriteLine("Getting participants");
                    Participants = (await API.Client.GetParticipants(Eveniment.Id)).ToList();

                    Console.WriteLine("----------------------------");
                    Participants.ForEach(Console.WriteLine);
                    Console.WriteLine("----------------------------");
                    ParticipantListView.Adapter = new ParticipantAdapter(this, Participants);
                });
            }
            catch (Exception e)
            {
                await MessageBox.Alert(this, $"GET request failed. Connection possibly failed\n\n{e.Message}", "Error");
            }
            //GetParticipants
        }

        private void VolButtonAddParticipant_Click(object sender, EventArgs e)
        {

        }
    }
}