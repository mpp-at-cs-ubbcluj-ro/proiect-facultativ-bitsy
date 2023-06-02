using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Views.InputMethods;
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
using static Android.App.DatePickerDialog;

namespace HelpingHands
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme")]
    internal class ViewEvenimentOrganizatorActivity : Activity, IOnDateSetListener
    {
        Eveniment Eveniment;

        EditText VolEvenimentName;
        EditText VolEvenimentDescription;        
        Button VolButtonAddParticipant;
        ListView ParticipantListView;
        Spinner InterestBox;
        EditText StartDateBox;
        EditText EndDateBox;
        EditText TargetDateBox;
        EditText LocatieBox;

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
            VolButtonAddParticipant = FindViewById<Button>(Resource.Id.VolButtonAddParticipant);
            InterestBox = FindViewById<Spinner>(Resource.Id.InterestBox);

            StartDateBox = FindViewById<EditText>(Resource.Id.StartDateBox);
            EndDateBox = FindViewById<EditText>(Resource.Id.EndDateBox);
            LocatieBox = FindViewById<EditText>(Resource.Id.LocatieBox);
            

            VolEvenimentName.Text = Eveniment.Name;
            VolEvenimentDescription.Text = Eveniment.Description;
            StartDateBox.Text = Eveniment.StartDate.ToString("dd.MM.yyyy");
            EndDateBox.Text = Eveniment.EndDate.ToString("dd.MM.yyyy");
            LocatieBox.Text = Eveniment.Location;

            VolButtonAddParticipant.Click += VolButtonAddParticipant_Click;

            ParticipantListView.SetOnTouchListener(new ListTouchListener());

            StartDateBox.Click += DateBox_Click;
            EndDateBox.Click += DateBox_Click;
            StartDateBox.Touch += DateBox_Touch;
            EndDateBox.Touch += DateBox_Touch;            
            StartDateBox.FocusChange += DateBox_FocusChange;
            EndDateBox.FocusChange += DateBox_FocusChange;

            Task.Run(Load);
        }

        private void DateBox_FocusChange(object sender, View.FocusChangeEventArgs e)
        {
            if (!e.HasFocus) return;
            TargetDateBox = sender as EditText;
            var dateTimeNow = DateTime.Now;
            DatePickerDialog datePicker = new DatePickerDialog(this, this, dateTimeNow.Year, dateTimeNow.Month, dateTimeNow.Day);
            datePicker.Show();
        }

        private void DateBox_Touch(object sender, View.TouchEventArgs e)
        {
            View view = this.CurrentFocus;
            if (view != null)
            {
                InputMethodManager imm = (InputMethodManager)GetSystemService(Context.InputMethodService);
                imm.HideSoftInputFromWindow(view.WindowToken, 0);
            }

            var input = (sender as EditText);
            var inType = input.InputType;
            input.InputType = Android.Text.InputTypes.Null;
            input.OnTouchEvent(e.Event);
            input.InputType = inType;
            e.Handled = true;
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
                    Console.WriteLine("Getting interests");
                    var interests = (await API.Client.GetInterests()).ToList();
                    InterestBox.Adapter = new InterestAdapter(this, interests);

                    if (Eveniment.Interests.Length > 0) 
                    {
                        var interest = interests.Where(_ => _.Name == Eveniment.Interests[0]).First();
                        InterestBox.SetSelection(interests.IndexOf(interest));
                    }

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

        private void DateBox_Click(object sender, EventArgs e)
        {
            TargetDateBox = sender as EditText;
            var dateTimeNow = DateTime.Now;
            DatePickerDialog datePicker = new DatePickerDialog(this, this, dateTimeNow.Year, dateTimeNow.Month, dateTimeNow.Day);
            datePicker.Show();
        }

        public void OnDateSet(DatePicker view, int year, int month, int dayOfMonth)
        {
            TargetDateBox.Text = new DateTime(year, month, dayOfMonth).ToString("dd.MM.yyyy");
        }
    }
}