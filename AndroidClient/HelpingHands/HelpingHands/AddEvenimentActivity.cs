using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Google.Android.Material.BottomNavigation;
using HelpingHands.Adapters;
using HelpingHands.API;
using HelpingHands.Data;
using HelpingHands.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static Android.App.DatePickerDialog;

namespace HelpingHands
{    
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme")]
    public class AddEvenimentActivity : Activity, IOnDateSetListener
    {
        EditText NumeBox;
        EditText DescriereBox;
        EditText StartDateBox;
        EditText EndDateBox;
        EditText TargetDateBox;
        Spinner InterestBox;
        EditText LocatieBox;
        Button AddEventButton;

        InterestAdapter InterestAdapter;

        public void OnDateSet(DatePicker view, int year, int month, int dayOfMonth)
        {
            TargetDateBox.Text = new DateTime(year, month, dayOfMonth).ToString("dd.MM.yyyy");                               
        }

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_add_eveniment);

            NumeBox = FindViewById<EditText>(Resource.Id.NumeBox);
            DescriereBox = FindViewById<EditText>(Resource.Id.DescriereBox);
            LocatieBox = FindViewById<EditText>(Resource.Id.LocatieBox);
            InterestBox = FindViewById<Spinner>(Resource.Id.InterestBox);

            StartDateBox = FindViewById<EditText>(Resource.Id.StartDateBox);
            EndDateBox = FindViewById<EditText>(Resource.Id.EndDateBox);
            AddEventButton = FindViewById<Button>(Resource.Id.AddEventButton);

            AddEventButton.Click += AddEventButton_Click;

            //

            //InterestBox.Adapter = 
            Task.Run(Load);
            

            StartDateBox.Click += DateBox_Click;
            EndDateBox.Click += DateBox_Click;
        }

        private async void AddEventButton_Click(object sender, EventArgs e)
        {
            //AddEveniment
            var nume = NumeBox.Text;
            var desc = DescriereBox.Text;
            var startDate = DateTime.ParseExact(StartDateBox.Text, "dd.MM.yyyy", null);
            var endDate = DateTime.ParseExact(EndDateBox.Text, "dd.MM.yyyy", null);

            var loc = LocatieBox.Text;
            var interest = new Interest[] { (InterestBox.Adapter as InterestAdapter)[InterestBox.SelectedItemPosition] };
            var ev = new Eveniment
            {
                Name = nume,
                Description = desc,
                StartDate = startDate,
                EndDate = endDate,
                Location = loc,
                Interests = interest.Select(_ => _.Name).ToArray(),
                InitiatorId = AppSession.UserId,
                Status = "PENDING"
            };
            try
            {
                await Client.AddEveniment(ev);
                await MessageBox.Alert(this, "Evenimentul a fost adaugat cu succes");

                var intent = new Intent(this, typeof(MainVoluntarActivity));
                intent.PutExtra("tab", 1);
                StartActivity(intent);                
            }
            catch(Exception ex)
            {
                await MessageBox.Alert(this, ex.Message);
            }



            //var 
        }

        private async void Load()
        {
            InterestAdapter = new InterestAdapter(this, (await API.Client.GetInterests()).ToList());
            RunOnUiThread(() => InterestBox.Adapter = InterestAdapter);
        }

        private void DateBox_Click(object sender, EventArgs e)
        {
            TargetDateBox = sender as EditText;
            var dateTimeNow = DateTime.Now;
            DatePickerDialog datePicker = new DatePickerDialog(this, this, dateTimeNow.Year, dateTimeNow.Month, dateTimeNow.Day);
            datePicker.Show();
        }
    }
}