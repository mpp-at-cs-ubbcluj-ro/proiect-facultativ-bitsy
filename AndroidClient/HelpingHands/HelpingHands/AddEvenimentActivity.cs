using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Google.Android.Material.BottomNavigation;
using HelpingHands.Adapters;
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
            TargetDateBox.Text = new DateTime(year, month, dayOfMonth).ToShortDateString();
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

            //

            //InterestBox.Adapter = 
            Task.Run(Load);
            

            StartDateBox.Click += DateBox_Click;
            EndDateBox.Click += DateBox_Click;
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