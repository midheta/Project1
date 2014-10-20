package com.example.android.Project1;

public class SessionData {

	
		int Id;
		byte[] ObjData;
		String SubjRating;
		String Notes;
		String DateTime;
		
		public SessionData() {
			
		}
		
		public SessionData(int id, byte[] objData, String subjRating, String notes, String dateTime) {
			this.Id = id;
			this.ObjData = objData;
			this.SubjRating = subjRating;
			this.Notes = notes;
			this.DateTime = dateTime;
		
		}

		public SessionData(byte[] objData, String subjRating, String notes, String dateTime) {
			this.ObjData = objData;
			this.SubjRating = subjRating;
			this.Notes = notes;
			this.DateTime = dateTime;
		
		}


		public int getId() {
			return Id;
		}

		public void setId(int id) {
			Id = id;
		}

		public byte[] getObjData() {
			return ObjData;
		}

		public void setObjData(byte[] objData) {
			this.ObjData = objData;
		}

		public String getSubjRating() {
			return SubjRating;
		}

		public void setSubjRating(String subjRating) {
			this.SubjRating = subjRating;
		}

		public String getNotes() {
			return Notes;
		}

		public void setNotes(String notes) {
			Notes = notes;
		}

		public String getDateTime() {
			return DateTime;
		}

		public void setDateTime(String dateTime) {
			DateTime = dateTime;
		}


}
