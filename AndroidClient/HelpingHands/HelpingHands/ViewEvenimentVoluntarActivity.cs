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
    public class ViewEvenimentVoluntarActivity : Activity
    {
        Eveniment Eveniment;

        TextView VolEvenimentName;
        TextView VolEvenimentDescription;
        TextView VolEvenimentDate;
        Button VolButtonAddParticipant;
        ListView ParticipantListView;

        List<Participant> Participants = new List<Participant>();

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_view_eveniment_voluntar);

            Eveniment = JsonConvert.DeserializeObject<Eveniment>(Intent.GetStringExtra("eveniment"));

            ParticipantListView = FindViewById<ListView>(Resource.Id.ParticipantListView);

            VolEvenimentName = FindViewById<TextView>(Resource.Id.VolEvenimentName);
            VolEvenimentDescription = FindViewById<TextView>(Resource.Id.VolEvenimentDescription);
            VolEvenimentDate = FindViewById<TextView>(Resource.Id.VolEvenimentDate);
            VolButtonAddParticipant = FindViewById<Button>(Resource.Id.VolButtonAddParticipant);

            VolEvenimentName.Text = Eveniment.Name;
            VolEvenimentDescription.Text = Eveniment.Description;
            VolEvenimentDate.Text = Eveniment.StartDate + " - " + Eveniment.EndDate;

            VolButtonAddParticipant.Click += VolButtonAddParticipant_Click;

            Task.Run(Load);
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