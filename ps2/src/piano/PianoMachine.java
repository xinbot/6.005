package piano;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sound.midi.MidiUnavailableException;

import midi.Instrument;
import midi.Midi;
import music.NoteEvent;
import music.Pitch;
import music.PitchLevel;

public class PianoMachine {
	
	private Midi midi;
	
	private Instrument instrument = Midi.DEFAULT_INSTRUMENT;
	
	private PitchLevel pitchLevel = PitchLevel.DEFAULT_LEVEL;
	
	public boolean isRecording = false;
	
	public List<NoteEvent> recordedEvents = new ArrayList<NoteEvent>();
	
	/**
	 * constructor for PianoMachine.
	 * 
	 * initialize midi device and any other state that we're storing.
	 */
    public PianoMachine() {
    	try {
            midi = Midi.getInstance();
        } catch (MidiUnavailableException e1) {
            System.err.println("Could not initialize midi device");
            e1.printStackTrace();
            return;
        }
    }
    
    /**
     * Turn an note event associated with the specified pitch.
     * @param rawPitch: the frequency of a musical note, required to
     * 					be among the set {0, 1, 2, ..., 10, 11}
     * */
    public void beginNote(Pitch rawPitch) {
    	long time = getCurrentTime();
    	NoteEvent event = new NoteEvent(rawPitch, time, instrument, NoteEvent.Kind.start);
    	
    	if(isRecording){
    		recordedEvents.add(event);
    	}
    	
    	Pitch modulatedPitch = modulatePitch(rawPitch);
    	midi.beginNote(modulatedPitch.toMidiFrequency(), instrument);
    }
    
    /**
     * Turn off an note event associated with the specified pitch.
     * @param rawPitch: the frequency of a musical note, required to
     * 					be among the set {0, 1, 2, ..., 10, 11}
     * */
    public void endNote(Pitch rawPitch) {
    	long time = getCurrentTime();
    	NoteEvent event = new NoteEvent(rawPitch, time, instrument, NoteEvent.Kind.stop);
    	
    	if(isRecording){
    		recordedEvents.add(event);
    	}
    	
    	Pitch modulatedPitch = modulatePitch(rawPitch);
    	midi.endNote(modulatedPitch.toMidiFrequency(), instrument);
    }
    
    /**
    * Switch the instrument mode to the next instrument among list
     * of musical instruments. If the current instrument is the last
     * one in the musical instrument list, switch back to the first
     * instrument.
     * */
    public void changeInstrument() {
    		this.instrument = instrument.next();
    }
    
    /**
     * Shift the notes that the keys play up, respectively, by one
     * octave (12 semitones).
     * */
    public void shiftUp() {
    	switch (pitchLevel) {
    	case TWO_OCTAVES_BELOW:
    		pitchLevel = PitchLevel.ONE_OCTAVE_BELOW;
    		return;
    	case ONE_OCTAVE_BELOW:
    		pitchLevel = PitchLevel.DEFAULT_LEVEL;
    		return;
    	case DEFAULT_LEVEL:
    		pitchLevel = PitchLevel.ONE_OCTAVE_ABOVE;
    		return;
    	case ONE_OCTAVE_ABOVE:
    		pitchLevel = PitchLevel.TWO_OCTAVES_ABOVE;
    		return;
    	case TWO_OCTAVES_ABOVE:
    		return;
    	}
    }
    
    /**
     * Shift the notes that the keys play down, respectively, by one
     * octave (12 semitones).
     * */
    public void shiftDown() {
    	switch (pitchLevel) {
    	case TWO_OCTAVES_BELOW:
    		return;
    	case ONE_OCTAVE_BELOW:
    		pitchLevel = PitchLevel.TWO_OCTAVES_BELOW;
    		return;
    	case DEFAULT_LEVEL:
    		pitchLevel = PitchLevel.ONE_OCTAVE_BELOW;
    		return;
    	case ONE_OCTAVE_ABOVE:
    		pitchLevel = PitchLevel.DEFAULT_LEVEL;
    		return;
    	case TWO_OCTAVES_ABOVE:
    		pitchLevel = PitchLevel.ONE_OCTAVE_ABOVE;
    		return;
    	}
    }
    
    
    /**
     * toggle the recording flag on and off, overriding previous
     * record if a new one is being made
     * @return true if the piano machine begins recording
     * 			otherwise false
     * */
    public boolean toggleRecording() {
    	if(isRecording){
    		isRecording = false;
    	}else{
    		isRecording = true;
    		recordedEvents = new ArrayList<NoteEvent>();
    	}
    	return isRecording;
    }
    
    /**
     * Playback sequences of recorded notes.
     */
    protected void playback() {    	
        if(recordedEvents.isEmpty()){
        	return;
        }else{
        	int eventAmount = recordedEvents.size();
        	if(eventAmount == 1){
        		executeNoteEvent(recordedEvents.get(0));
        	}else{
        		long[] delayTimeArray = new long[eventAmount-1];
    			delayTimeArray = getDelayTimeArray(recordedEvents);
    			
    			for (int i = 0; i < eventAmount-1; i = i + 1) {
    				// play the all but the last note event
    				NoteEvent event = recordedEvents.get(i);
    				executeNoteEvent(event);
    				Midi.wait((int)delayTimeArray[i]);
    			}
    			// last note event
    			NoteEvent lastEvent = recordedEvents.get(eventAmount-1);
    			executeNoteEvent(lastEvent);
        	}
        }
    }
    
    /**
     * Return the system's current time in millisecond.
     */
    private long getCurrentTime() {
    	Calendar cal = new GregorianCalendar();
    	return cal.getTimeInMillis();
    }
    
    /**
     * Modulate the rawPitch to an appropriate modulatedPitch according to the
     * piano machine's pitch level (in octaves).
     * @param rawPitch standard pitch of a musical note
     * @return modulatedPitch the resulting pitch after modulation
     */
    private Pitch modulatePitch(Pitch rawPitch) {
    	Pitch modulatedPitch = rawPitch;
    	switch (pitchLevel) {
    	case DEFAULT_LEVEL:
    		modulatedPitch = rawPitch;
    		break;
    	case ONE_OCTAVE_BELOW:
    		modulatedPitch = rawPitch.transpose((Pitch.OCTAVE) * (-1));
    		break;
    	case TWO_OCTAVES_BELOW:
    		modulatedPitch =
    			rawPitch.transpose((Pitch.OCTAVE) * (-1)).transpose((Pitch.OCTAVE) * (-1));
    		break;
    	case ONE_OCTAVE_ABOVE:
    		modulatedPitch = rawPitch.transpose(Pitch.OCTAVE);
    		break;
    	case TWO_OCTAVES_ABOVE:
    		modulatedPitch =
    			rawPitch.transpose(Pitch.OCTAVE).transpose(Pitch.OCTAVE);
    		break;
    	}
    	return modulatedPitch;
    }
    
    /**
     * Play a note, invoked by playback
     */
    protected void executeNoteEvent(NoteEvent event) {
    	if (event.getKind() == NoteEvent.Kind.start) {
			beginNote(event.getPitch());
		} else {
			endNote(event.getPitch());
		}
    }
    
    /**
     * Returns an array of delay time between note events.
     */
    protected long[] getDelayTimeArray(List<NoteEvent> eventList) {
    	int eventAmout = eventList.size();
    	long[] delayTimeArray = new long[eventAmout-1];
    	for (int i = 0; i < eventAmout-1; i++) {
    		long elapsedTime =
        			(long) Math.floor((double)(eventList.get(i+1).getTime() - eventList.get(i).getTime()) / 10.0);
        	delayTimeArray[i] = elapsedTime;
		}
    	return delayTimeArray;
    }
}
